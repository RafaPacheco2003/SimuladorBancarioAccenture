package com.simulador.financiero.dtos.request;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TransactionRequest(
        @Schema(description = "Número de cuenta propia (16 dígitos) de la que sale el dinero", example = "1234567890123456")
        @NotBlank(message = "La cuenta de origen es obligatoria")
        @Size(min = 16, max = 16, message = "La cuenta de origen debe tener 16 dígitos")
        String originAccount,

        @Schema(description = "Número de cuenta destino (16 dígitos)", example = "6543210987654321")
        @NotBlank(message = "La cuenta de destino es obligatoria")
        @Size(min = 16, max = 16, message = "La cuenta de destino debe tener 16 dígitos")
        String destinationAccount,

        @Schema(description = "Monto a transferir, mínimo 0.01", example = "250.50")
        @NotNull(message = "El monto es obligatorio")
        @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
        BigDecimal amount,

        @Schema(description = "Concepto de la transferencia, máximo 100 caracteres", example = "Pago de renta")
        @NotBlank(message = "El concepto es obligatorio")
        @Size(max = 100, message = "El concepto no puede exceder 100 caracteres")
        String concept
){}
