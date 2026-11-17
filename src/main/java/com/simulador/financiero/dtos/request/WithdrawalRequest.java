package com.simulador.financiero.dtos.request;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record WithdrawalRequest(

    @Schema(description = "Número de cuenta (16 dígitos) de la que se realiza el retiro", example = "1234567890123456")
    @NotBlank(message = "La cuenta es obligatoria")
    @Size(min = 16, max = 16, message = "La cuenta debe tener 16 caracteres")
    String account,

    @Schema(description = "Monto a retirar, mínimo 0.01", example = "500.00")
    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a cero")
    BigDecimal amount,

    @Schema(description = "Concepto del movimiento, máximo 100 caracteres", example = "Retiro en cajero")
    @NotBlank(message = "El concepto es obligatorio")
    @Size(max = 100, message = "El concepto no puede exceder 100 caracteres")
    String concept
) {

}
