package com.simulador.financiero.DTOs.response;

import java.math.BigDecimal;

import com.simulador.financiero.entities.TransactionStatus;

public record ComprobanteResponse(
  Long id,
  String originAccount,
  String destinationAccount,
  BigDecimal amount,
  TransactionStatus transactionStatus
) {
}

