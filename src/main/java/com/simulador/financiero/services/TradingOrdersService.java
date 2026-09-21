package com.simulador.financiero.services;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simulador.financiero.DTOs.request.TradingOrderRequest;
import com.simulador.financiero.DTOs.response.TradingOrderResponse;
import com.simulador.financiero.Exceptions.BadRequestException;
import com.simulador.financiero.Exceptions.ResourceNotFoundException;
import com.simulador.financiero.constants.ExceptionMessageConstants;
import com.simulador.financiero.entities.AccountEntity;
import com.simulador.financiero.entities.ActionEntity;
import com.simulador.financiero.entities.StocksEntity;
import com.simulador.financiero.entities.TradingOrdersEntity;
import com.simulador.financiero.mappers.TradingOrderMapper;
import com.simulador.financiero.repositories.ActionRepository;
import com.simulador.financiero.repositories.StockRespository;
import com.simulador.financiero.repositories.TradingOrdersRepository;
import com.simulador.financiero.validators.TradingOrderValidator;
import com.simulador.financiero.validators.TransferValidator;

@Service
public class TradingOrdersService implements ITradingOrderService {

    private final IAccountService accountService;
    private final StockRespository stockRepository;
    private final ActionRepository actionRepository;
    private final TradingOrdersRepository tradingOrdersRepository;
    private final TradingOrderMapper tradingOrderMapper;

    public TradingOrdersService(
            IAccountService accountService,
            StockRespository stockRepository,
            ActionRepository actionRepository,
            TradingOrdersRepository tradingOrdersRepository,
            TradingOrderMapper tradingOrderMapper) {
        this.accountService = accountService;
        this.stockRepository = stockRepository;
        this.actionRepository = actionRepository;
        this.tradingOrdersRepository = tradingOrdersRepository;
        this.tradingOrderMapper = tradingOrderMapper;
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

        accountService.deposit(account, totalAmount);

        TradingOrdersEntity order = tradingOrderMapper.toEntity(
                account, stock, request.quantity(), executionPrice, totalAmount);

        return tradingOrderMapper.toResponse(tradingOrdersRepository.save(order));
    }

    private AccountEntity getOwnedActiveAccount(Long userId, String accountNumber) {
        AccountEntity account = accountService.findByNumber(accountNumber);
        TransferValidator.validateOwnership(account, userId);
        TransferValidator.validateIsActive(account);
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
