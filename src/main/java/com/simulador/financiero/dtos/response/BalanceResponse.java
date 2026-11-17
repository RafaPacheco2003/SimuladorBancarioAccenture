package com.simulador.financiero.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.simulador.financiero.account.Currency;

import io.swagger.v3.oas.annotations.media.Schema;


public record BalanceResponse(
    @Schema(description = "Número de cuenta de 16 dígitos", example = "1234567890123456")
    String number,
    @Schema(description = "Divisa de la cuenta", example = "MXN")
    Currency currency,
    @Schema(description = "Saldo actual de la cuenta", example = "10500.75")
    BigDecimal balance,
    @Schema(description = "Fecha de creación de la cuenta", example = "2026-01-15T10:30:00")
    LocalDateTime createdAt)
{

}
