package com.simulador.financiero.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record BuyActionRequest(
    @NotBlank(message = "El ticker es obligatorio")
    String ticker,

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a cero")
    Integer quantity
){}
