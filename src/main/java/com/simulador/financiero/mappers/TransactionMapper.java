package com.simulador.financiero.mappers;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.simulador.financiero.DTOs.response.ComprobanteResponse;
import com.simulador.financiero.DTOs.response.TransactionHistResponse;
import com.simulador.financiero.entities.AccountEntity;
import com.simulador.financiero.entities.TransactionEntity;
import com.simulador.financiero.entities.TransactionStatus;

@Component
public class TransactionMapper {

    public TransactionEntity toEntity(
            AccountEntity origin,
            AccountEntity destination,
            BigDecimal originalAmount,
            BigDecimal destinationAmount,
            BigDecimal exchangeRate,
            String concept) {

        return TransactionEntity.builder()
                .originAccount(origin)
                .destinationAccount(destination)
                .originalAmount(originalAmount)
                .originCurrency(origin.getCurrency())
                .destinationAmount(destinationAmount)
                .destinationCurrency(destination.getCurrency())
                .exchangeRate(exchangeRate)
                .concept(concept)
                .status(TransactionStatus.COMPLETED)
                .build();
    }

    public TransactionEntity toDepositEntity(AccountEntity account, BigDecimal amount, String concept) {

        return TransactionEntity.builder()
                .originAccount(account)
                .destinationAccount(account)
                .originalAmount(amount)
                .originCurrency(account.getCurrency())
                .destinationAmount(amount)
                .destinationCurrency(account.getCurrency())
                .exchangeRate(BigDecimal.ONE)
                .concept(concept)
                .status(TransactionStatus.COMPLETED)
                .build();
    }

    public ComprobanteResponse toComprobante(TransactionEntity entity) {
        return new ComprobanteResponse(
                entity.getId(),
                entity.getOriginAccount().getNumber(),
                entity.getDestinationAccount().getNumber(),
                entity.getOriginalAmount(),
                entity.getStatus());
    }

    public TransactionHistResponse toTransactionHist(TransactionEntity entity) {
        return new TransactionHistResponse(
                entity.getOriginAccount().getNumber(),
                entity.getDestinationAccount().getNumber(),
                entity.getOriginalAmount(),
                entity.getStatus(),
                entity.getDate());
    }
}
