package com.simulador.financiero.dtos.request;

import com.simulador.financiero.account.AccountType;
import com.simulador.financiero.account.Currency;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateAccountRequest(
        @Schema(description = "Tipo de cuenta", example = "Saving")
        AccountType accountType,
        @Schema(description = "Divisa de la cuenta", example = "MXN")
        Currency currency
) {}
