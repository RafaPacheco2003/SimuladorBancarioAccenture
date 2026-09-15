package com.simulador.financiero.services;

import java.math.BigDecimal;

import com.simulador.financiero.account.AccountType;
import com.simulador.financiero.account.Currency;
import com.simulador.financiero.entities.AccountEntity;

public interface IAccountService {

    public String createAccount(
        Long userId,
        AccountType accountType,
        Currency currency
    );

    AccountEntity findByNumber(String number);

    void withdraw(AccountEntity account, BigDecimal amount);

    void deposit(AccountEntity account, BigDecimal amount);

}
