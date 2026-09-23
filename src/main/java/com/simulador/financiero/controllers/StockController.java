package com.simulador.financiero.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.simulador.financiero.DTOs.response.StockResponse;
import com.simulador.financiero.services.IStockService;
import com.simulador.financiero.DTOs.response.ErrorDetail;

import lombok.AllArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stocks")
@AllArgsConstructor
@Tag(name = "Stocks-controller", description = "Operaciones relacionadas con acciones.")
public class StockController {

    private final IStockService stockService;

    @GetMapping("/consulta")
    @Operation(summary = "Consulta de Acciones", description = "Obtiene la lista de todas las acciones.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Consulta exitosa", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StockResponse.class))),
        @ApiResponse(responseCode = "401", description = "El endpoint requiere autenticación.", content = @Content(schema = @Schema(implementation = ErrorDetail.class)))
    })
    public ResponseEntity<List<StockResponse>> consultaAcciones(){
        
        List<StockResponse> response = stockService.consultaAcciones();
        return ResponseEntity.ok(response);

    }

}
