package com.simulador.financiero.services;

import com.simulador.financiero.DTOs.request.TransactionRequest;
import com.simulador.financiero.DTOs.request.CashMovementRequest;
import com.simulador.financiero.DTOs.response.ComprobanteResponse;
import com.simulador.financiero.DTOs.response.TransactionHistResponse;

import java.util.List;

public interface ITransactionService {

  public ComprobanteResponse performTransfer(Long userId, TransactionRequest request);

  public ComprobanteResponse performDeposit(Long userId, CashMovementRequest request);

  List<TransactionHistResponse> getTransactionsHistory(String accountNumber);
}
