package com.simulador.financiero.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.simulador.financiero.entities.TransactionStatus;

import io.swagger.v3.oas.annotations.media.Schema;

public record ComprobanteResponse(
  @Schema(description = "Identificador de la transacción", example = "42")
  Long id,
  @Schema(description = "Cuenta de origen", example = "1234567890123456")
  String originAccount,
  @Schema(description = "Cuenta de destino", example = "6543210987654321")
  String destinationAccount,
  @Schema(description = "Monto de la transacción", example = "250.50")
  BigDecimal amount,
  @Schema(description = "Estado de la transacción", example = "COMPLETED")
  TransactionStatus transactionStatus,
  @Schema(description = "Concepto de la transacción", example = "Pago de renta")
  String concept,
  @Schema(description = "Fecha y hora de la transacción", example = "2026-01-15T10:30:00")
  LocalDateTime date
) {
}

