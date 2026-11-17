package com.simulador.financiero.dtos.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record WithdrawalRequest(
    
    @NotBlank(message = "La cuenta es obligatoria")
    @Size(min = 16, max = 16, message = "La cuenta debe tener 16 caracteres")
    String account,

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a cero")
    BigDecimal amount,

    @NotBlank(message = "El concepto es obligatorio")
    @Size(max = 100, message = "El concepto no puede exceder 100 caracteres")
    String concept
) {

}
