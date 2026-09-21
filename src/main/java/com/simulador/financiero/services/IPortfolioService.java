package com.simulador.financiero.services;

import com.simulador.financiero.DTOs.response.PortfolioSummaryResponse;

public interface IPortfolioService {

    PortfolioSummaryResponse getPortfolioSummary(Long userId, String accountNumber);

}
