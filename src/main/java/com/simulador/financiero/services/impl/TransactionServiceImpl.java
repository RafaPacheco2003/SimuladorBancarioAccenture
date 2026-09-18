package com.simulador.financiero.services.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simulador.financiero.DTOs.request.CashMovementRequest;
import com.simulador.financiero.DTOs.request.TransactionRequest;
import com.simulador.financiero.DTOs.request.WithdrawalRequest;
import com.simulador.financiero.DTOs.response.ComprobanteResponse;
import com.simulador.financiero.DTOs.response.TransactionHistResponse;
import com.simulador.financiero.entities.AccountEntity;
import com.simulador.financiero.entities.TransactionEntity;
import com.simulador.financiero.mappers.TransactionMapper;
import com.simulador.financiero.repositories.TransactionRepository;
import com.simulador.financiero.services.IAccountService;
import com.simulador.financiero.services.IExchangeRateService;
import com.simulador.financiero.services.ITransactionService;
import com.simulador.financiero.validators.TransferValidator;

@Service
public class TransactionServiceImpl implements ITransactionService {

        private final IAccountService accountService;
        private final IExchangeRateService exchangeRateService;
        private final TransactionRepository transactionRepository;
        private final TransactionMapper transactionMapper;

        public TransactionServiceImpl(
                        IAccountService accountService,
                        IExchangeRateService exchangeRateService,
                        TransactionRepository transactionRepository,
                        TransactionMapper transactionMapper) {
                this.accountService = accountService;
                this.exchangeRateService = exchangeRateService;
                this.transactionRepository = transactionRepository;
                this.transactionMapper = transactionMapper;
        }

        @Override
        @Transactional
        public ComprobanteResponse performTransfer(Long userId, TransactionRequest request) {

                AccountEntity origin = accountService.findByNumber(request.originAccount());
                TransferValidator.validateIsActive(origin);
                TransferValidator.validateOwnership(origin, userId);

                AccountEntity destination = accountService.findByNumber(request.destinationAccount());
                TransferValidator.validateIsActive(destination);
                TransferValidator.validateDifferentOwner(origin, destination);

                TransferValidator.validateDifferentAccounts(origin.getNumber(), destination.getNumber());
                TransferValidator.validateAmount(request.amount());
                TransferValidator.validateSufficientBalance(origin, request.amount());

                BigDecimal exchangeRate = exchangeRateService.exchangeRate(
                                origin.getCurrency(), destination.getCurrency());

                BigDecimal destinationAmount = exchangeRateService.convert(
                                request.amount(), origin.getCurrency(), destination.getCurrency());

                accountService.withdraw(origin, request.amount());
                accountService.deposit(destination, destinationAmount);

                TransactionEntity transaction = transactionMapper.toEntity(
                                origin, destination, request.amount(), destinationAmount,
                                exchangeRate, request.concept());

                return transactionMapper.toComprobante(transactionRepository.save(transaction));
        }

        @Override
        @Transactional
        public ComprobanteResponse performDeposit(Long userId, CashMovementRequest request) {

                AccountEntity account = accountService.findByNumber(request.Account());
                TransferValidator.validateIsActive(account);
                TransferValidator.validateOwnership(account, userId);
                TransferValidator.validateAmount(request.amount());

                accountService.deposit(account, request.amount());

                TransactionEntity transaction = transactionMapper.toDepositEntity(
                                account, request.amount(), request.concept());

                return transactionMapper.toComprobante(transactionRepository.save(transaction));
        }

        @Override
        @Transactional(readOnly = true)
        public List<TransactionHistResponse> getTransactionsHistory(String accountNumber) {
                return transactionRepository.findByOriginAccount_Number(accountNumber)
                                .stream()
                                .map(transactionMapper::toTransactionHist)
                                .collect(Collectors.toList());
        }

        @Override
        @Transactional
        public ComprobanteResponse performWithdrawal(Long userId, WithdrawalRequest request) {
                
                AccountEntity account = accountService.findByNumber(request.Account());
                TransferValidator.validateOwnership(account, userId);
                TransferValidator.validateIsActive(account);
                TransferValidator.validateAmount(request.Amount());
                TransferValidator.validateSufficientBalance(account, request.Amount());
                
                accountService.withdraw(account, request.Amount());

                TransactionEntity transaction = transactionMapper.toWithdrawal(account, request.Amount(), request.Concept());
                
                return transactionMapper.toComprobante(transactionRepository.save(transaction));
        }        
}

