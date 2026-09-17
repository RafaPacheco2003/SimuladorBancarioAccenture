package com.simulador.financiero.DTOs.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.simulador.financiero.entities.TransactionStatus;

public record TransactionHistResponse(
  String originAccount,
  String destinationAccount,
  BigDecimal amount,
  TransactionStatus transactionStatus,
  LocalDateTime createdAt) 
{

}
