package com.simulador.financiero.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simulador.financiero.config.security.AuthenticatedUser;
import com.simulador.financiero.services.IAccountService;
import com.simulador.financiero.utils.AuthenticatedUserProvider;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/v1/account")
@AllArgsConstructor
public class AccountController {

    private final IAccountService accountService;

    @PostMapping
    public String createAccount() {
        AuthenticatedUser authenticatedUser = AuthenticatedUserProvider.getAuthenticatedUser();

        accountService.createAccount(authenticatedUser.id());

        return "created";
    }

}
