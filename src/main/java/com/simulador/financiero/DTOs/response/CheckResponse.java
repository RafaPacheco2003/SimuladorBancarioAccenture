package com.simulador.financiero.DTOs.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.simulador.financiero.account.Currency;


public record CheckResponse(
    String number,
    Currency currency, 
    BigDecimal balance, 
    LocalDateTime createdAt)
{

}