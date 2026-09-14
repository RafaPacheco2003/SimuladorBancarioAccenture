package com.simulador.financiero.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simulador.financiero.DTOs.request.CreateAccountRequest;
import com.simulador.financiero.config.security.AuthenticatedUser;
import com.simulador.financiero.services.IAccountService;
import com.simulador.financiero.utils.AuthenticatedUserProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/v1/account")
@AllArgsConstructor
@Tag(name = "Account", description = "Gestión de cuentas bancarias")
@SecurityRequirement(name = "bearerAuth")
public class AccountController {

    private final IAccountService accountService;


        @PostMapping
        @Operation(summary = "Crear una cuenta bancaria", description = "Crea una cuenta bancaria para el usuario autenticado.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Cuenta creada correctamente", content = @Content(schema = @Schema(type = "string", example = "Account successfully created"))),
                        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
                        @ApiResponse(responseCode = "409", description = "No se pudo crear la cuenta por un conflicto", content = @Content(schema = @Schema(type = "string")))
        })
        public String createAccount(@RequestBody CreateAccountRequest request) {

        AuthenticatedUser authenticatedUser =
        AuthenticatedUserProvider.getAuthenticatedUser();

        accountService.createAccount(
                authenticatedUser.id(),
                request.accountType(),
                request.currency()
        );

        return "Account successfully created";
        }

}