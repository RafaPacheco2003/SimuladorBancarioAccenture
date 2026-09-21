package com.simulador.financiero.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simulador.financiero.DTOs.response.ErrorDetail;
import com.simulador.financiero.DTOs.response.PortfolioSummaryResponse;
import com.simulador.financiero.config.security.AuthenticatedUser;
import com.simulador.financiero.services.IPortfolioService;
import com.simulador.financiero.utils.AuthenticatedUserProvider;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/cuentas")
@AllArgsConstructor
@Tag(name = "Portfolio-controller", description = "Consulta de posiciones y ganancia/pérdida del portafolio")
public class PortfolioController {

    private final IPortfolioService portfolioService;

    @GetMapping("/{numeroCuenta}/portafolio")
    @Operation(summary = "Consultar resumen del portafolio de una cuenta", description = "Retorna cada posición de la cuenta con su ganancia/pérdida respecto al precio actual, junto con un resumen agregado del portafolio.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Resumen del portafolio (vacío si no hay posiciones)"),
            @ApiResponse(responseCode = "403", description = "La cuenta no pertenece al usuario autenticado", content = @Content(schema = @Schema(implementation = ErrorDetail.class))),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada", content = @Content(schema = @Schema(implementation = ErrorDetail.class)))
    })
    public PortfolioSummaryResponse getPortfolio(@PathVariable String numeroCuenta) {
        AuthenticatedUser authenticatedUser = AuthenticatedUserProvider.getAuthenticatedUser();
        return portfolioService.getPortfolioSummary(authenticatedUser.id(), numeroCuenta);
    }
}
