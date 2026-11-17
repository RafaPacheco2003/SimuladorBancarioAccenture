package com.simulador.financiero.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TradingOrderResponse(
    String ticker,
    int quantity,
    BigDecimal executionPrice,
    BigDecimal totalAmount,
    BigDecimal newBalance,
    LocalDateTime date
){}
