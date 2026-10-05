package com.simulador.financiero.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simulador.financiero.DTOs.request.TradingOrderRequest;
import com.simulador.financiero.DTOs.response.ErrorDetail;
import com.simulador.financiero.DTOs.response.TradingOrderResponse;
import com.simulador.financiero.config.security.AuthenticatedUser;
import com.simulador.financiero.services.ITradingOrderService;
import com.simulador.financiero.utils.AuthenticatedUserProvider;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/trading")
@AllArgsConstructor
@Tag(name = "Trading-controller", description = "compra y venta de acciones")
public class TradingOrderController {

    private final ITradingOrderService tradingOrderService;

    @PostMapping("/account/{accountNumber}/actions/sell")
    @Operation(summary = "Vender acciones", description = "Vende una cantidad de acciones de un ticker de una cuenta propia y abona el monto total de la venta al saldo de la cuenta.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Venta realizada", content = @Content(schema = @Schema(implementation = TradingOrderResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos, cuenta bloqueada o acciones insuficientes", content = @Content(schema = @Schema(implementation = ErrorDetail.class))),
            @ApiResponse(responseCode = "403", description = "La cuenta no pertenece al usuario autenticado", content = @Content(schema = @Schema(implementation = ErrorDetail.class))),
            @ApiResponse(responseCode = "404", description = "Cuenta o acción no encontrada", content = @Content(schema = @Schema(implementation = ErrorDetail.class)))
    })
    public ResponseEntity<TradingOrderResponse> sellActions(
            @PathVariable String accountNumber,
            @Valid @RequestBody TradingOrderRequest request) {

        AuthenticatedUser authenticatedUser = AuthenticatedUserProvider.getAuthenticatedUser();

        TradingOrderResponse response = tradingOrderService.sellActions(
                authenticatedUser.id(), accountNumber, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
