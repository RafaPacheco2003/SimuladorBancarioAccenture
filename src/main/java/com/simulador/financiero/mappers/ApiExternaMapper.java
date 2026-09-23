package com.simulador.financiero.mappers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.simulador.financiero.DTOs.external.GlobalQuoteDTO;
import com.simulador.financiero.DTOs.response.StockResponse;
import com.simulador.financiero.entities.StocksEntity;

@Component
public class ApiExternaMapper {

    private static final DateTimeFormatter TRADING_DAY_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

    public StocksEntity toEntity(GlobalQuoteDTO quote) {
        return updateEntity(new StocksEntity(), quote);
    }

    public StocksEntity updateEntity(StocksEntity entity, GlobalQuoteDTO quote) {
        entity.setTicker(quote.symbol());
        entity.setCompanyName(quote.symbol());
        entity.setCurrentPrice(new BigDecimal(quote.price()));
        entity.setUpdatedAt(LocalDate.parse(quote.latestTradingDay(), TRADING_DAY_FORMAT).atStartOfDay());
        return entity;
    }

    public StockResponse toResponse(StocksEntity entity) {
        return new StockResponse(
                entity.getId(),
                entity.getTicker(),
                entity.getCompanyName(),
                entity.getCurrentPrice(),
                entity.getUpdatedAt());
    }
}
