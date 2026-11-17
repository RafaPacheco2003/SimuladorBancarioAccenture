package com.simulador.financiero.services;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.simulador.financiero.dtos.response.ConsultApiExchangeResponse;
import com.simulador.financiero.account.Currency;

@Service 
public class ConsultApiExchangeService {
        
    private final RestClient restClient;

    public ConsultApiExchangeService(
            @Value("${app.exchange.api-base-url:https://api.frankfurter.dev}") String baseUrl) {
        this.restClient = RestClient.builder()
            .baseUrl(baseUrl)
            .build();
    }


    public BigDecimal consultarApi(Currency from, Currency to){
        ConsultApiExchangeResponse response = restClient
        .get()
        .uri("/v1/latest?from={from}&to={to}", from.name(), to.name())
        .retrieve()
        .body(ConsultApiExchangeResponse.class);

        return response != null ? response.rates().get(to.name()) : null;

    }
    
}