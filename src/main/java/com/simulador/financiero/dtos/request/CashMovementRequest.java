package com.simulador.financiero.dtos.request;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CashMovementRequest(
  @Schema(description = "Número de cuenta (16 dígitos) donde se realiza el depósito", example = "1234567890123456")
  @NotBlank(message = "La cuenta de origen es obligatoria")
  String account,
  @Schema(description = "Monto a depositar, mínimo 0.01", example = "1500.00")
  @NotNull(message = "El monto es obligatorio")
  @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
  BigDecimal amount,
  @Schema(description = "Concepto del movimiento, máximo 100 caracteres", example = "Depósito en ventanilla")
  @NotBlank(message = "El concepto es obligatorio")
  @Size(max = 100)
  String concept
){}
