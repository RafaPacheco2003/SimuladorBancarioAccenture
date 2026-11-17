package com.simulador.financiero.dtos.response;

import java.math.BigDecimal;

public record ActivePositionResponse(
    String ticker,
    String companyName,
    Integer quantity,
    BigDecimal averageBuyPrice,
    BigDecimal currentPrice,
    BigDecimal currentValue,
    BigDecimal gainLoss,
    BigDecimal gainLossPercentage) {
}
