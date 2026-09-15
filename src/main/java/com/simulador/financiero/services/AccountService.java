package com.simulador.financiero.services;

import java.math.BigDecimal;

import com.simulador.financiero.Exceptions.ResourceNotFoundException;
import com.simulador.financiero.account.AccountType;
import com.simulador.financiero.account.Currency;
import com.simulador.financiero.constants.ExceptionMessageConstants;
import com.simulador.financiero.entities.AccountEntity;
import com.simulador.financiero.entities.UserEntity;
import com.simulador.financiero.repositories.AccountRepository;
import com.simulador.financiero.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements IAccountService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public void createAccount(Long userId, AccountType accountType, Currency currency) {

        UserEntity user = userRepository.getReferenceById(userId);

        AccountEntity account = AccountEntity.builder()
                .user(user)
                .currency(currency)
                .accountType(accountType)
                .build();

        accountRepository.save(account);
    }

    @Override
    public AccountEntity findByNumber(String number) {
        return accountRepository.findById(number)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ExceptionMessageConstants.ACCOUNT_NOT_FOUND));
    }

    @Override
    public void withdraw(AccountEntity account, BigDecimal amount) {
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);
    }

    @Override
    public void deposit(AccountEntity account, BigDecimal amount) {
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
    }
}