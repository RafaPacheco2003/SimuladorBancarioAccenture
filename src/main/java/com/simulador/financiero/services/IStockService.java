package com.simulador.financiero.services;

import java.util.List;
import com.simulador.financiero.dtos.response.StockResponse;

public interface IStockService {

    public List<StockResponse> consultaAcciones();

}
