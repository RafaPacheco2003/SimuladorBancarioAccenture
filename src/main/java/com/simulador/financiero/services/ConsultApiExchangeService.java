package com.simulador.financiero.services;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.simulador.financiero.DTOs.response.ConsultApiExchangeResponse;
import com.simulador.financiero.account.Currency;

@Service 
public class ConsultApiExchangeService {
        
    RestClient restClient= RestClient.builder()
        .baseUrl("https://api.frankfurter.dev")
        .build();


    public BigDecimal consultarApi(Currency from, Currency to){
        ConsultApiExchangeResponse response = restClient
        .get()
        .uri("/v1/latest?from={from}&to={to}", from.name(), to.name())
        .retrieve()
        .body(ConsultApiExchangeResponse.class);

        return response != null ? response.rates().get(to.name()) : null;

    }
    
}