package com.simulador.financiero.services;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

import com.simulador.financiero.Exceptions.BadRequestException;
import com.simulador.financiero.account.Currency;
import com.simulador.financiero.constants.ExceptionMessageConstants;

import jakarta.transaction.Transactional;

@Service
public class ExchangeRateService implements IExchangeRateService {

    private static final int RATE_SCALE = 6;
    private static final int AMOUNT_SCALE = 2;

    // private BigDecimal mxnToUsd;
    // private BigDecimal usdMxn;
    private ConsultApiExchangeService consultApiExchangeService;


    public ExchangeRateService(ConsultApiExchangeService consultApiExchangeService) {
        this.consultApiExchangeService = consultApiExchangeService;
    }
    
    @Override
    @Transactional 
    public BigDecimal exchangeRate(Currency origin, Currency destination) {
        
        // usdMxn = consultApiExchangeService.consultarApi(origin, destination);
        // mxnToUsd = consultApiExchangeService.consultarApi(origin, destination);

        if (origin == destination) {
            return BigDecimal.ONE.setScale(RATE_SCALE);
        }

        return consultApiExchangeService.consultarApi(origin, destination);

        // if (origin == Currency.MXN && destination == Currency.USD) {
        //     return mxnToUsd.setScale(RATE_SCALE, RoundingMode.HALF_UP);
        // }



        // if (origin == Currency.USD && destination == Currency.MXN) {
        //     return usdMxn.setScale(RATE_SCALE, RoundingMode.HALF_UP);
        // }

        // throw new BadRequestException(ExceptionMessageConstants.UNSUPPORTED_EXCHANGE_RATE);
    }

    @Override
    public BigDecimal convert(BigDecimal amount, Currency origin, Currency destination) {
        return amount.multiply(exchangeRate(origin, destination))
                .setScale(AMOUNT_SCALE, RoundingMode.HALF_UP);
    }
}