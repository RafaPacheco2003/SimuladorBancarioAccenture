package com.simulador.financiero.controllers;

import com.simulador.financiero.config.security.AuthenticatedUser;
import com.simulador.financiero.entities.AccountEntity;
import com.simulador.financiero.services.IAccountService;
import com.simulador.financiero.utils.AuthenticatedUserProvider;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/account")
@AllArgsConstructor
public class AccountController {

    private final IAccountService accountService;

    @PostMapping
    public String createAccount(
            @RequestParam AccountEntity.TipoCuenta tipoCuenta,
            @RequestParam AccountEntity.Moneda moneda) {

        AuthenticatedUser authenticatedUser =
                AuthenticatedUserProvider.getAuthenticatedUser();

        accountService.createAccount(
                authenticatedUser.id(),
                tipoCuenta,
                moneda
        );

        return "created";
    }
}

