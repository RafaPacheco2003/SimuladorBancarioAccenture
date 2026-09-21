package com.simulador.financiero.DTOs.response;

import java.math.BigDecimal;
import java.util.List;

public record PortfolioSummaryResponse(
    List<Position> positions,
    BigDecimal investedValue,
    BigDecimal cashBalance,
    BigDecimal totalGainLossPercentage) {

    public record Position(
        String ticker,
        String companyName,
        Integer quantity,
        BigDecimal averageBuyPrice,
        BigDecimal currentPrice,
        BigDecimal currentValue,
        BigDecimal gainLoss,
        BigDecimal gainLossPercentage) {
    }
}
