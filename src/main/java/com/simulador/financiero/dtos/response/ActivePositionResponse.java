package com.simulador.financiero.dtos.response;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

public record ActivePositionResponse(
    @Schema(description = "Símbolo (ticker) de la acción", example = "AAPL")
    String ticker,
    @Schema(description = "Nombre de la empresa", example = "Apple Inc.")
    String companyName,
    @Schema(description = "Cantidad de acciones en posesión", example = "5")
    Integer quantity,
    @Schema(description = "Precio promedio de compra por acción", example = "185.00")
    BigDecimal averageBuyPrice,
    @Schema(description = "Precio actual por acción", example = "189.50")
    BigDecimal currentPrice,
    @Schema(description = "Valor actual de la posición", example = "947.50")
    BigDecimal currentValue,
    @Schema(description = "Ganancia o pérdida absoluta", example = "22.50")
    BigDecimal gainLoss,
    @Schema(description = "Ganancia o pérdida en porcentaje", example = "2.43")
    BigDecimal gainLossPercentage) {
}
