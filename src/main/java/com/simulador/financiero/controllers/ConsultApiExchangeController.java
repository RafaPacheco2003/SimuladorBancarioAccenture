package com.simulador.financiero.controllers;

import java.math.BigDecimal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import com.simulador.financiero.services.ConsultApiExchangeService;
import com.simulador.financiero.services.ExchangeRateService;
import com.simulador.financiero.services.impl.ConsultarApiExternaServiceImpl;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController 
@Controller 
@RequestMapping("/api/v1/exchange")
public class ConsultApiExchangeController{
    private final ConsultApiExchangeService consultApiExchangeService;
    private final ExchangeRateService exchangeRateService;

    public ConsultApiExchangeController(ConsultApiExchangeService consultApiExchangeService,
            ExchangeRateService exchangeRateService) {
        this.consultApiExchangeService = consultApiExchangeService;
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping()
    public BigDecimal consultarApi(){
        return consultApiExchangeService.consultarApi();
    }
    
}