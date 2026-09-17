package com.simulador.financiero.DTOs.request;

import com.simulador.financiero.account.AccountType;
import com.simulador.financiero.account.Currency;

public record CreateAccountRequest(
        AccountType accountType,
        Currency currency
) {}