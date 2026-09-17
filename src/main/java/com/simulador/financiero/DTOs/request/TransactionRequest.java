package com.simulador.financiero.DTOs.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TransactionRequest(
        @NotBlank(message = "La cuenta de origen es obligatoria")
        @Size(min = 16, max = 16, message = "La cuenta de origen debe tener 16 dígitos")
        String originAccount,

        @NotBlank(message = "La cuenta de destino es obligatoria")
        @Size(min = 16, max = 16, message = "La cuenta de destino debe tener 16 dígitos")
        String destinationAccount,

        @NotNull(message = "El monto es obligatorio")
        @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
        BigDecimal amount,

        @NotBlank(message = "El concepto es obligatorio")
        @Size(max = 100, message = "El concepto no puede exceder 100 caracteres")
        String concept
){}
