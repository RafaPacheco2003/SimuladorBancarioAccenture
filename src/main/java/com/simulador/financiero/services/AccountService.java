package com.simulador.financiero.services;

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

    public void createAccount(Long userId, AccountEntity.TipoCuenta tipoCuenta, AccountEntity.Moneda moneda) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        AccountEntity account = AccountEntity.builder()
                .user(user)
                .moneda(moneda)
                .tipoCuenta(tipoCuenta)
                .build();

        accountRepository.save(account);
    }
}







