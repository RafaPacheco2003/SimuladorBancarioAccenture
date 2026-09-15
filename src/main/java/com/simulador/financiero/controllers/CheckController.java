package com.simulador.financiero.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.simulador.financiero.DTOs.response.CheckResponse;
import com.simulador.financiero.services.CheckService;

@RestController
public class CheckController {
    private final CheckService checkService;

    public CheckController(CheckService checkService) {
        this.checkService = checkService;
    }

    @GetMapping("/api/v1/cuentas/{numeroCuenta}/saldo")
    public CheckResponse getBalance(@PathVariable String numeroCuenta){

        return checkService.getAccountBalance(numeroCuenta);
    }
}