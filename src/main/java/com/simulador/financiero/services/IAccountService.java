package com.simulador.financiero.services;

import com.simulador.financiero.account.AccountType;
import com.simulador.financiero.account.Currency;

public interface IAccountService {

    public void createAccount(
        Long userId,
        AccountType accountType,
        Currency currency
    );

}