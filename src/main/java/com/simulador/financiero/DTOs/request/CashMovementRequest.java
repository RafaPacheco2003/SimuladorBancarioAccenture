package com.simulador.financiero.DTOs.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CashMovementRequest(
  @NotBlank(message = "La cuenta de origen es obligatoria")
  String Account,
  @NotNull(message = "El monto es obligatorio")
  @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
  BigDecimal amount,
  @NotBlank(message = "El concepto es obligatorio")
  @Size(max = 100)
  String concept
){}
