package com.simulador.financiero.dtos.request;

import com.simulador.financiero.account.AccountType;
import com.simulador.financiero.account.Currency;

public record CreateAccountRequest(
        AccountType accountType,
        Currency currency
) {}