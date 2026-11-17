package com.simulador.financiero.services;

import com.simulador.financiero.dtos.response.StockResponse;

public interface IConsultarApiExternaService {

    StockResponse consultarYGuardarCotizacion(String symbol);
}
