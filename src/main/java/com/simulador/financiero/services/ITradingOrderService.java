package com.simulador.financiero.services;

import com.simulador.financiero.DTOs.request.TradingOrderRequest;
import com.simulador.financiero.DTOs.response.TradingOrderResponse;

public interface ITradingOrderService {

    TradingOrderResponse sellActions(Long userId, String accountNumber, TradingOrderRequest request);

}
