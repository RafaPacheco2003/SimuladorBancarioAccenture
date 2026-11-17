package com.simulador.financiero.dtos.response;

import java.math.BigDecimal;
import java.util.Map;

public record ConsultApiExchangeResponse(
    BigDecimal amount,
    String base,
    String date,
    Map<String, BigDecimal> rates
) {}