package com.simulador.financiero.DTOs.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record StockResponse(
    Long id,
    String ticker,
    String companyName,
    BigDecimal currentPrice,
    LocalDateTime updatedAt) {
}
