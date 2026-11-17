package com.simulador.financiero.services;

import com.simulador.financiero.dtos.request.TransactionRequest;
import com.simulador.financiero.dtos.request.CashMovementRequest;
import com.simulador.financiero.dtos.response.ComprobanteResponse;
import com.simulador.financiero.dtos.response.TransactionHistResponse;
import com.simulador.financiero.dtos.request.WithdrawalRequest;

import java.util.List;

public interface ITransactionService {

  public ComprobanteResponse performTransfer(Long userId, TransactionRequest request);

  public ComprobanteResponse performDeposit(Long userId, CashMovementRequest request);

  List<TransactionHistResponse> getTransactionsHistory(Long userId, String accountNumber);

  public ComprobanteResponse performWithdrawal(Long userId, WithdrawalRequest request);

}
