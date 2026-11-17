package com.simulador.financiero.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record BuyActionRequest(
    @Schema(description = "Símbolo (ticker) de la acción a comprar", example = "AAPL")
    @NotBlank(message = "El ticker es obligatorio")
    String ticker,

    @Schema(description = "Cantidad de acciones a comprar, mayor a cero", example = "5")
    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a cero")
    Integer quantity
){}
