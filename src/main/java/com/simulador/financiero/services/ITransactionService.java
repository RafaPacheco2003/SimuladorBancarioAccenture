package com.simulador.financiero.services;

import com.simulador.financiero.DTOs.request.TransactionRequest;
import com.simulador.financiero.DTOs.response.ComprobanteResponse;
import com.simulador.financiero.DTOs.response.TransactionHistResponse;

import java.util.List;

public interface ITransactionService {

  public ComprobanteResponse performTransaction(Long userId, TransactionRequest request);

  List<TransactionHistResponse> getTransactionsHistory(String accountNumber);
}
