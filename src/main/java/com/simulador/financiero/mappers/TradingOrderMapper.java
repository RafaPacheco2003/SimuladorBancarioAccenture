package com.simulador.financiero.mappers;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.simulador.financiero.DTOs.response.TradingOrderResponse;
import com.simulador.financiero.entities.AccountEntity;
import com.simulador.financiero.entities.StocksEntity;
import com.simulador.financiero.entities.TradingOrdersEntity;

@Component
public class TradingOrderMapper {

    public TradingOrdersEntity toEntity(
            AccountEntity account,
            StocksEntity stock,
            int quantity,
            BigDecimal executionPrice,
            BigDecimal totalAmount) {

        return TradingOrdersEntity.builder()
                .account(account)
                .stock(stock)
                .quantity(quantity)
                .price(executionPrice)
                .totalAmount(totalAmount)
                .build();
    }

    public TradingOrderResponse toResponse(TradingOrdersEntity entity) {
        return new TradingOrderResponse(
                entity.getStock().getTicker(),
                entity.getQuantity(),
                entity.getPrice(),
                entity.getTotalAmount(),
                entity.getAccount().getBalance(),
                entity.getCreated());
    }
}
