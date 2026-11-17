package com.simulador.financiero.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TradingOrderRequest(
    @Schema(description = "Símbolo (ticker) de la acción a vender", example = "AAPL")
    @NotBlank(message = "El ticker es obligatorio")
    String ticker,

    @Schema(description = "Cantidad de acciones a vender, mayor a cero", example = "5")
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a cero")
    Integer quantity
){}
