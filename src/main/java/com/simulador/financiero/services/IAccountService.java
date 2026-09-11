package com.simulador.financiero.services;

import com.simulador.financiero.entities.AccountEntity;

public interface IAccountService {
    public void createAccount(Long userId, AccountEntity.TipoCuenta tipocuenta, AccountEntity.Moneda moneda);
}

