package com.simulador.financiero.services;

import com.simulador.financiero.DTOs.request.TransactionRequest;
import com.simulador.financiero.DTOs.response.ComprobanteResponse;

public interface ITransactionService {

  public ComprobanteResponse performTransaction(Long userId, TransactionRequest request);
}
