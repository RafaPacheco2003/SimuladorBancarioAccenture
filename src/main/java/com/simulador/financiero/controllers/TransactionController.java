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

import com.simulador.financiero.dtos.request.CashMovementRequest;
import com.simulador.financiero.dtos.request.TransactionRequest;
import com.simulador.financiero.dtos.response.ComprobanteResponse;
import com.simulador.financiero.dtos.request.WithdrawalRequest;
import com.simulador.financiero.dtos.response.ErrorDetail;
import com.simulador.financiero.dtos.response.TransactionHistResponse;
import com.simulador.financiero.config.security.AuthenticatedUser;
import com.simulador.financiero.services.ITransactionService;
import com.simulador.financiero.config.security.AuthenticatedUserProvider;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/transactions")
@AllArgsConstructor
@Tag(name = "Transfers-controller", description = "transferencias, depositos y retiros")
public class TransactionController {

        private final ITransactionService transactionService;

        @PostMapping("/transfers")
        @Operation(summary = "Realizar una transferencia", description = "Transfiere un monto de una cuenta propia a una cuenta destino de otro usuario, aplicando el tipo de cambio cuando las divisas difieren.")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "Transferencia realizada", content = @Content(schema = @Schema(implementation = ComprobanteResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Datos inválidos o cuenta bloqueada", content = @Content(schema = @Schema(implementation = ErrorDetail.class))),
                        @ApiResponse(responseCode = "403", description = "La cuenta de origen no pertenece al usuario autenticado", content = @Content(schema = @Schema(implementation = ErrorDetail.class))),
                        @ApiResponse(responseCode = "404", description = "Cuenta no encontrada", content = @Content(schema = @Schema(implementation = ErrorDetail.class))),
                        @ApiResponse(responseCode = "422", description = "Saldo insuficiente", content = @Content(schema = @Schema(implementation = ErrorDetail.class)))
        })
        public ResponseEntity<ComprobanteResponse> performTransfer(
                        @Valid @RequestBody TransactionRequest request) {

                AuthenticatedUser authenticatedUser = AuthenticatedUserProvider.getAuthenticatedUser();

                ComprobanteResponse comprobante = transactionService.performTransfer(authenticatedUser.id(),
                                request);

                return ResponseEntity.status(HttpStatus.CREATED).body(comprobante);
        }

        @GetMapping("/{numeroCuenta}")
        @Operation(summary = "Consultar historial de transacciones", description = "Retorna las transacciones (transferencias, depósitos y retiros) de una cuenta propia del usuario autenticado.")
        @ApiResponse(responseCode = "200", description = "Historial de transacciones (vacío si no hay movimientos)", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TransactionHistResponse.class))))
        public List<TransactionHistResponse> getTransactionHistory(
                        @Parameter(description = "Número de cuenta de 16 dígitos", example = "1234567890123456") @PathVariable String numeroCuenta) {
                AuthenticatedUser authenticatedUser = AuthenticatedUserProvider.getAuthenticatedUser();

                return transactionService.getTransactionsHistory(authenticatedUser.id(), numeroCuenta);
        }

        @PostMapping("/deposits")
        @Operation(summary = "Realizar un depósito en efectivo", description = "Deposita un monto en una cuenta propia del usuario autenticado.")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "Depósito realizado", content = @Content(schema = @Schema(implementation = ComprobanteResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Datos inválidos o cuenta bloqueada", content = @Content(schema = @Schema(implementation = ErrorDetail.class))),
                        @ApiResponse(responseCode = "403", description = "La cuenta no pertenece al usuario autenticado", content = @Content(schema = @Schema(implementation = ErrorDetail.class))),
                        @ApiResponse(responseCode = "404", description = "Cuenta no encontrada", content = @Content(schema = @Schema(implementation = ErrorDetail.class)))
        })
        public ResponseEntity<ComprobanteResponse> performDeposit(
                        @Valid @RequestBody CashMovementRequest request) {

                AuthenticatedUser authenticatedUser = AuthenticatedUserProvider.getAuthenticatedUser();

                ComprobanteResponse comprobante = transactionService.performDeposit(authenticatedUser.id(), request);

                return ResponseEntity.status(HttpStatus.CREATED).body(comprobante);
        }

        @PostMapping("/withdrawals")
        @Operation(summary = "Realizar un retiro en efectivo", description = "Retira un monto de una cuenta propia del usuario autenticado.")
                @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "Retiro realizado", content = @Content(schema = @Schema(implementation = ComprobanteResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Datos inválidos o cuenta bloqueada", content = @Content(schema = @Schema(implementation = ErrorDetail.class))),
                        @ApiResponse(responseCode = "403", description = "La cuenta no pertenece al usuario autenticado", content = @Content(schema = @Schema(implementation = ErrorDetail.class))),
                        @ApiResponse(responseCode = "404", description = "Cuenta no encontrada", content = @Content(schema = @Schema(implementation = ErrorDetail.class))),
                        @ApiResponse(responseCode = "422", description = "Saldo insuficiente", content = @Content(schema = @Schema(implementation = ErrorDetail.class)))
        })
        public ResponseEntity<ComprobanteResponse> performWithdrawal(@Valid @RequestBody WithdrawalRequest request) {

                AuthenticatedUser authenticatedUser = AuthenticatedUserProvider.getAuthenticatedUser();

                ComprobanteResponse comprobante = transactionService.performWithdrawal(authenticatedUser.id(), request);

                return ResponseEntity.status(HttpStatus.CREATED).body(comprobante);
        }
}
