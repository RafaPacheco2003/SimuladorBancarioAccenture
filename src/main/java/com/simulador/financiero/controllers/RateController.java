package com.simulador.financiero.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import com.simulador.financiero.DTOs.request.RateLimitFilter;
import com.simulador.financiero.services.LimiterService;

@RestController 
@RequestMapping("/pruebafinal")
public class RateController {
    private final LimiterService limiterService;

    public RateController(LimiterService limiterService) {
        this.limiterService = limiterService;
    }

    @GetMapping()
    public void allow(){
       limiterService.allowRequest();
    }
}
