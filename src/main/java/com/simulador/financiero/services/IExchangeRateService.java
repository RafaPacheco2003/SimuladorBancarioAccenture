package com.simulador.financiero.services;

import java.math.BigDecimal;

import com.simulador.financiero.account.Currency;

public interface IExchangeRateService {

    BigDecimal exchangeRate(Currency origin, Currency destination);

    BigDecimal convert(BigDecimal amount, Currency origin, Currency destination);
}
