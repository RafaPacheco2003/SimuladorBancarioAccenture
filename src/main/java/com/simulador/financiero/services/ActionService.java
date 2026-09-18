package com.simulador.financiero.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simulador.financiero.Exceptions.ResourceNotFoundException;
import com.simulador.financiero.entities.AccountEntity;
import com.simulador.financiero.entities.ActionEntity;
import com.simulador.financiero.entities.StocksEntity;
import com.simulador.financiero.repositories.AccountRepository;
import com.simulador.financiero.repositories.ActionRepository;
import com.simulador.financiero.repositories.StockRespository;

@Service
public class ActionService {

    private final ActionRepository actionRepository;
    private final AccountRepository accountRepository;
    private final StockRespository stockRepository;

    public ActionService(
            ActionRepository actionRepository,
            AccountRepository accountRepository,
            StockRespository stockRepository) {

        this.actionRepository = actionRepository;
        this.accountRepository = accountRepository;
        this.stockRepository = stockRepository;
    }

    @Transactional
    public String buyAction(
            String ticker,
            Integer quantity,
            String accountNumber) {

        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException(
                    "The quantity must be greater than 0."
            );
        }

        StocksEntity stock = stockRepository
                .findByTickerIgnoreCase(ticker)
                .orElseThrow(() ->
                        new ResourceNotFoundException("stock not found"));

        AccountEntity account = accountRepository
                .findById(accountNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Account not found"));

        BigDecimal currentPrice = stock.getCurrentPrice();

        BigDecimal total = currentPrice.multiply(
                BigDecimal.valueOf(quantity.longValue())
        );

        if (account.getBalance().compareTo(total) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        account.setBalance(
                account.getBalance().subtract(total)
        );

        ActionEntity action = actionRepository
                .findByAccountAndStock(account, stock)
                .orElse(null);

        if (action == null) {

            action = ActionEntity.builder()
                    .account(account)
                    .stock(stock)
                    .quantity(quantity)
                    .average(currentPrice)
                    .build();

        } else {

            int oldQuantity = action.getQuantity();
            BigDecimal oldAverage = action.getAverage();

            BigDecimal newAverage = oldAverage
                    .multiply(BigDecimal.valueOf(oldQuantity))
                    .add(total)
                    .divide(
                            BigDecimal.valueOf(oldQuantity + quantity),
                            2,
                            RoundingMode.HALF_UP
                    );

            action.setQuantity(oldQuantity + quantity);
            action.setAverage(newAverage);
        }

        actionRepository.save(action);
        accountRepository.save(account);

        return "Purchase successful";
    }



    public List<ActionEntity> getAction(){
        return actionRepository.findAll();
    }

}