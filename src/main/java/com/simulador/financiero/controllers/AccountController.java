package com.simulador.financiero.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simulador.financiero.DTOs.request.CreateAccountRequest;
import com.simulador.financiero.config.security.AuthenticatedUser;
import com.simulador.financiero.entities.AccountEntity;
import com.simulador.financiero.services.IAccountService;
import com.simulador.financiero.utils.AuthenticatedUserProvider;
import org.springframework.web.bind.annotation.RequestBody;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/v1/account")
@AllArgsConstructor
public class AccountController {

    private final IAccountService accountService;


        @PostMapping
        public String createAccount(@RequestBody CreateAccountRequest request) {

        AuthenticatedUser authenticatedUser =
        AuthenticatedUserProvider.getAuthenticatedUser();

        String number= accountService.createAccount(
                authenticatedUser.id(),
                request.accountType(),
                request.currency()
        );

        return "Account successfully created and account number: " +number;
        }

        // aca se mapaearea el nuevo request que sera para poder realizar la venta de acctiones 
}