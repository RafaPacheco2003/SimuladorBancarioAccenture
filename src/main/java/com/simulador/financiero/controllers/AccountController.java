package com.simulador.financiero.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simulador.financiero.dtos.request.CreateAccountRequest;
import com.simulador.financiero.dtos.response.BalanceResponse;
import com.simulador.financiero.config.security.AuthenticatedUser;
import com.simulador.financiero.entities.AccountEntity;
import com.simulador.financiero.services.IAccountService;
import com.simulador.financiero.config.security.AuthenticatedUserProvider;
import org.springframework.web.bind.annotation.RequestBody;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/account")
@AllArgsConstructor
@Tag(name = "Account-controller", description = "Creación de cuentas y consulta de saldo")
public class AccountController {

    private final IAccountService accountService;

        @PostMapping("/create")
        @Operation(summary = "Crear una cuenta", description = "Crea una cuenta bancaria nueva para el usuario autenticado con el tipo y la divisa indicados.")
        @ApiResponse(responseCode = "200", description = "Cuenta creada; retorna un mensaje con el número de cuenta", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string", example = "Account successfully created and account number: 1234567890123456")))
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

        @GetMapping("/{number}/balance")
        @Operation(summary = "Consultar saldo de una cuenta", description = "Retorna el saldo, la divisa y la fecha de creación de una cuenta propia del usuario autenticado.")
        @ApiResponse(responseCode = "200", description = "Saldo de la cuenta", content = @Content(schema = @Schema(implementation = BalanceResponse.class)))
        public BalanceResponse getBalance(
                        @Parameter(description = "Número de cuenta de 16 dígitos", example = "1234567890123456") @PathVariable String number) {

        AuthenticatedUser authenticatedUser =
        AuthenticatedUserProvider.getAuthenticatedUser();

        return accountService.getBalance(authenticatedUser.id(), number);
        }

}