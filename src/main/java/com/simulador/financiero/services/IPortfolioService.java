package com.simulador.financiero.services;

import java.util.List;

import com.simulador.financiero.DTOs.response.ActivePositionResponse;

public interface IPortfolioService {

    List<ActivePositionResponse> getActivePositions(Long userId, String accountNumber);

}
