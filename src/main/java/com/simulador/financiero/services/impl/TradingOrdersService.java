package com.simulador.financiero.services.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simulador.financiero.dtos.request.BuyActionRequest;
import com.simulador.financiero.dtos.request.TradingOrderRequest;
import com.simulador.financiero.dtos.response.TradingOrderResponse;
import com.simulador.financiero.exceptions.BadRequestException;
import com.simulador.financiero.exceptions.InsufficientBalanceException;
import com.simulador.financiero.exceptions.ResourceNotFoundException;
import com.simulador.financiero.account.Currency;
import com.simulador.financiero.constants.ExceptionMessageConstants;
import com.simulador.financiero.entities.AccountEntity;
import com.simulador.financiero.entities.ActionEntity;
import com.simulador.financiero.entities.StocksEntity;
import com.simulador.financiero.entities.TradingOrdersEntity;
import com.simulador.financiero.mappers.TradingOrderMapper;
import com.simulador.financiero.repositories.ActionRepository;
import com.simulador.financiero.repositories.StockRepository;
import com.simulador.financiero.repositories.TradingOrdersRepository;
import com.simulador.financiero.validators.AccountValidator;
import com.simulador.financiero.validators.TradingOrderValidator;
import com.simulador.financiero.services.IAccountService;
import com.simulador.financiero.services.IExchangeRateService;
import com.simulador.financiero.services.ITradingOrderService;

@Service
public class TradingOrdersService implements ITradingOrderService {

    private final IAccountService accountService;
    private final StockRepository stockRepository;
    private final ActionRepository actionRepository;
    private final TradingOrdersRepository tradingOrdersRepository;
    private final TradingOrderMapper tradingOrderMapper;
    private final IExchangeRateService exchangeRateService;

    public TradingOrdersService(
            IAccountService accountService,
            StockRepository stockRepository,
            ActionRepository actionRepository,
            TradingOrdersRepository tradingOrdersRepository,
            TradingOrderMapper tradingOrderMapper,
            IExchangeRateService exchangeRateService) {
        this.accountService = accountService;
        this.stockRepository = stockRepository;
        this.actionRepository = actionRepository;
        this.tradingOrdersRepository = tradingOrdersRepository;
        this.tradingOrderMapper = tradingOrderMapper;
        this.exchangeRateService = exchangeRateService;
    }

    @Override
    @Transactional
    public TradingOrderResponse buyActions(Long userId, String accountNumber, BuyActionRequest request) {

        StocksEntity stock = getStock(request.ticker());
        AccountEntity account = accountService.findByNumber(accountNumber);
        AccountValidator.validateOwnership(account, userId);

        int quantity = request.quantity();
        BigDecimal currentPrice = stock.getCurrentPrice();
        BigDecimal totalUsd = currentPrice.multiply(BigDecimal.valueOf(quantity));

        BigDecimal totalForAccount = exchangeRateService.convert(
                totalUsd, Currency.USD, account.getCurrency());

        if (account.getBalance().compareTo(totalForAccount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        accountService.withdraw(account, totalForAccount);

        ActionEntity action = actionRepository.findByAccountAndStock(account, stock).orElse(null);

        if (action == null) {
            action = ActionEntity.builder()
                    .account(account)
                    .stock(stock)
                    .quantity(quantity)
                    .average(currentPrice)
                    .build();
        } else {
            int oldQuantity = action.getQuantity();
            BigDecimal newAverage = action.getAverage()
                    .multiply(BigDecimal.valueOf(oldQuantity))
                    .add(totalUsd)
                    .divide(BigDecimal.valueOf(oldQuantity + quantity), 2, RoundingMode.HALF_UP);

            action.setQuantity(oldQuantity + quantity);
            action.setAverage(newAverage);
        }

        actionRepository.save(action);

        // No TradingOrdersEntity is persisted: it has no BUY/SELL side yet, so a buy row
        // would be indistinguishable from a sell. Build the same response directly.
        return new TradingOrderResponse(
                stock.getTicker(),
                quantity,
                currentPrice,
                calculateTotal(currentPrice, quantity),
                account.getBalance(),
                LocalDateTime.now());
    }

    @Override
    @Transactional
    public TradingOrderResponse sellActions(Long userId, String accountNumber, TradingOrderRequest request) {

        AccountEntity account = getOwnedActiveAccount(userId, accountNumber);
        StocksEntity stock = getStock(request.ticker());
        ActionEntity position = getPosition(account, stock);
        TradingOrderValidator.validateSufficientActions(position, request.quantity());

        BigDecimal executionPrice = stock.getCurrentPrice();
        BigDecimal totalAmount = calculateTotal(executionPrice, request.quantity());

        position.setQuantity(position.getQuantity() - request.quantity());
        actionRepository.save(position);

        BigDecimal amountForAccount = exchangeRateService.convert(
                totalAmount, Currency.USD, account.getCurrency());
        accountService.deposit(account, amountForAccount);

        TradingOrdersEntity order = tradingOrderMapper.toEntity(
                account, stock, request.quantity(), executionPrice, totalAmount);

        return tradingOrderMapper.toResponse(tradingOrdersRepository.save(order));
    }

    private AccountEntity getOwnedActiveAccount(Long userId, String accountNumber) {
        AccountEntity account = accountService.findByNumber(accountNumber);
        TradingOrderValidator.validateOwnership(account, userId);
        TradingOrderValidator.validateIsActive(account);
        return account;
    }

    private StocksEntity getStock(String ticker) {
        return stockRepository.findByTickerIgnoreCase(ticker)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessageConstants.STOCK_NOT_FOUND));
    }

    private ActionEntity getPosition(AccountEntity account, StocksEntity stock) {
        ActionEntity position = actionRepository.findByAccountAndStock(account, stock)
                .orElseThrow(() -> new BadRequestException(ExceptionMessageConstants.NO_ACTIONS_FOR_TICKER));
        TradingOrderValidator.validateHasActions(position);
        return position;
    }

    private BigDecimal calculateTotal(BigDecimal price, int quantity) {
        return price.multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.HALF_UP);
    }
}
