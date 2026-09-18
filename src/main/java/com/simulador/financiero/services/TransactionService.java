package com.simulador.financiero.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.simulador.financiero.DTOs.response.TransactionHistResponse;
import com.simulador.financiero.mappers.TransactionMapper;
import com.simulador.financiero.repositories.TransactionRepository;

@Service
public class TransactionService {
/*     private final TransactionRepository transactionRepo;
    private final TransactionMapper transactionMapper;
    
    public TransactionService(TransactionRepository transactionRepo, TransactionMapper transactionMapper) {
        this.transactionRepo = transactionRepo;
        this.transactionMapper = transactionMapper;
    }

    public List<TransactionHistResponse> getTransactionsHistory(String accountNumber) {
        List<TransactionHistResponse> TransactionHistory;
        
        TransactionHistory = transactionRepo.findByOriginAccount_Number(accountNumber)
                .stream()
                .map(transactionMapper::toTransactionHist)
                .collect(Collectors.toList());

        if (TransactionHistory.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.OK, "No se encontraron transacciones para la cuenta: " + accountNumber);
        }
        return TransactionHistory;
    } */

}
