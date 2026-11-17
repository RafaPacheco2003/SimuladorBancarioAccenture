package com.simulador.financiero.services;

import com.simulador.financiero.dtos.request.BuyActionRequest;
import com.simulador.financiero.dtos.request.TradingOrderRequest;
import com.simulador.financiero.dtos.response.TradingOrderResponse;

public interface ITradingOrderService {

    String buyActions(Long userId, String accountNumber, BuyActionRequest request);

    TradingOrderResponse sellActions(Long userId, String accountNumber, TradingOrderRequest request);

}
