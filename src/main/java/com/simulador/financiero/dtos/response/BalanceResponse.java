package com.simulador.financiero.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.simulador.financiero.account.Currency;


public record BalanceResponse(
    String number,
    Currency currency, 
    BigDecimal balance, 
    LocalDateTime createdAt)
{

}