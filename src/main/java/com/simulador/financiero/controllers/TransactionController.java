package com.simulador.financiero.controllers;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import com.simulador.financiero.DTOs.request.TransactionRequest;
import com.simulador.financiero.DTOs.response.ComprobanteResponse;
import com.simulador.financiero.DTOs.response.ErrorDetail;
import com.simulador.financiero.DTOs.response.TransactionHistResponse;
import com.simulador.financiero.config.security.AuthenticatedUser;
import com.simulador.financiero.services.ITransactionService;
import com.simulador.financiero.utils.AuthenticatedUserProvider;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import com.simulador.financiero.entities.AccountEntity;

@RestController
@RequestMapping("/api/v1/transferencias")
@AllArgsConstructor
@Tag(name = "Transferencias", description = "Transferencias entre cuentas")
public class TransactionController {

    private final ITransactionService transactionService;

    @PostMapping
    @Operation(summary = "Realizar una transferencia",
            description = "Transfiere un monto de una cuenta propia a una cuenta destino de otro usuario, aplicando el tipo de cambio cuando las divisas difieren.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Transferencia realizada",
                    content = @Content(schema = @Schema(implementation = ComprobanteResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o cuenta bloqueada",
                    content = @Content(schema = @Schema(implementation = ErrorDetail.class))),
            @ApiResponse(responseCode = "403", description = "La cuenta de origen no pertenece al usuario autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorDetail.class))),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorDetail.class))),
            @ApiResponse(responseCode = "422", description = "Saldo insuficiente",
                    content = @Content(schema = @Schema(implementation = ErrorDetail.class)))
    })
    public ResponseEntity<ComprobanteResponse> performTransaction(
            @Valid @RequestBody TransactionRequest request) {

        AuthenticatedUser authenticatedUser = AuthenticatedUserProvider.getAuthenticatedUser();

        ComprobanteResponse comprobante =
                transactionService.performTransaction(authenticatedUser.id(), request);

        return ResponseEntity.status(HttpStatus.CREATED).body(comprobante);
    }

        @GetMapping("/{numeroCuenta}")
    public List<TransactionHistResponse> getTransactionHistory(@PathVariable String numeroCuenta){
                return transactionService.getTransactionsHistory(numeroCuenta);
    }
    
}
