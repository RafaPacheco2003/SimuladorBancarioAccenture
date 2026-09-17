package com.simulador.financiero.services;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.simulador.financiero.Exceptions.BadRequestException;
import com.simulador.financiero.account.Currency;
import com.simulador.financiero.constants.ExceptionMessageConstants;

@Service
public class ExchangeRateService implements IExchangeRateService {

    private static final int RATE_SCALE = 6;
    private static final int AMOUNT_SCALE = 2;

    private final BigDecimal mxnToUsd;
    private final BigDecimal usdToMxn;

    public ExchangeRateService(
            @Value("${app.exchange.mxn-usd:0.055000}") BigDecimal mxnToUsd,
            @Value("${app.exchange.usd-mxn:18.000000}") BigDecimal usdToMxn) {
        this.mxnToUsd = mxnToUsd;
        this.usdToMxn = usdToMxn;
    }

    @Override
    public BigDecimal exchangeRate(Currency origin, Currency destination) {

        if (origin == destination) {
            return BigDecimal.ONE.setScale(RATE_SCALE);
        }

        if (origin == Currency.MXN && destination == Currency.USD) {
            return mxnToUsd.setScale(RATE_SCALE, RoundingMode.HALF_UP);
        }

        if (origin == Currency.USD && destination == Currency.MXN) {
            return usdToMxn.setScale(RATE_SCALE, RoundingMode.HALF_UP);
        }

        throw new BadRequestException(ExceptionMessageConstants.UNSUPPORTED_EXCHANGE_RATE);
    }

    @Override
    public BigDecimal convert(BigDecimal amount, Currency origin, Currency destination) {
        return amount.multiply(exchangeRate(origin, destination))
                .setScale(AMOUNT_SCALE, RoundingMode.HALF_UP);
    }
}
