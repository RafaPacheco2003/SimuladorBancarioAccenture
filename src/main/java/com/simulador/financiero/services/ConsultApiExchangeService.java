package com.simulador.financiero.services;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.simulador.financiero.DTOs.response.ConsultApiExchangeResponse;

@Service 
public class ConsultApiExchangeService {
    
    RestClient restClient= RestClient.builder()
        .baseUrl("https://api.frankfurter.dev")
        .build();

    public BigDecimal consultarApi(){
        ConsultApiExchangeResponse response = restClient
        .get()
        .uri("/v1/latest?from=USD&to=MXN")
        .retrieve()
        .body(ConsultApiExchangeResponse.class);

    return response != null ? response.rates().get("MXN") : null;
    }   
}