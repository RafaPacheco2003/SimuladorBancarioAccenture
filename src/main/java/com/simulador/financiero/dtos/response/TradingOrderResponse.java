package com.simulador.financiero.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

public record TradingOrderResponse(
    @Schema(description = "Símbolo (ticker) de la acción", example = "AAPL")
    String ticker,
    @Schema(description = "Cantidad de acciones operadas", example = "5")
    int quantity,
    @Schema(description = "Precio de ejecución por acción", example = "189.50")
    BigDecimal executionPrice,
    @Schema(description = "Monto total de la operación", example = "947.50")
    BigDecimal totalAmount,
    @Schema(description = "Saldo de la cuenta después de la operación", example = "9553.25")
    BigDecimal newBalance,
    @Schema(description = "Fecha y hora de la operación", example = "2026-01-15T10:30:00")
    LocalDateTime date
){}
