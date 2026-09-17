package com.simulador.financiero.services;

import com.simulador.financiero.DTOs.response.StockResponse;

public interface IConsultarApiExternaService {

    StockResponse consultarYGuardarCotizacion(String symbol);
}
