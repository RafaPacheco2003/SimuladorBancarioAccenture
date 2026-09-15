package com.simulador.financiero.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simulador.financiero.DTOs.response.StockResponse;
import com.simulador.financiero.services.IConsultarApiExternaService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/api-externa")
@AllArgsConstructor
public class ConsultarApiExternaController {

  private final IConsultarApiExternaService consultarApiExternaService;

  @GetMapping("/cotizacion/{symbol}")
  public StockResponse consultarCotizacion(@PathVariable String symbol) {
    return consultarApiExternaService.consultarYGuardarCotizacion(symbol);
  }


}
