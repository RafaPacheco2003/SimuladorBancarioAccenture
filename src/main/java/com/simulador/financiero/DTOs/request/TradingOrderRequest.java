package com.simulador.financiero.DTOs.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TradingOrderRequest(
    @NotBlank(message = "El ticker es obligatorio")
    String ticker,

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a cero")
    Integer quantity
){}
