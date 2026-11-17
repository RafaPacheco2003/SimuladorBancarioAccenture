package com.simulador.financiero.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

public record StockResponse(
    @Schema(description = "Identificador de la acción", example = "1")
    Long id,
    @Schema(description = "Símbolo (ticker) de la acción", example = "AAPL")
    String ticker,
    @Schema(description = "Nombre de la empresa", example = "Apple Inc.")
    String companyName,
    @Schema(description = "Precio actual por acción", example = "189.50")
    BigDecimal currentPrice,
    @Schema(description = "Fecha y hora de la última actualización del precio", example = "2026-01-15T10:30:00")
    LocalDateTime updatedAt) {
}
