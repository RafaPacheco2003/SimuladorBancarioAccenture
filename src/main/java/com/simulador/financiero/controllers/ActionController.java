package com.simulador.financiero.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.simulador.financiero.config.security.AuthenticatedUser;
import com.simulador.financiero.entities.ActionEntity;
import com.simulador.financiero.repositories.ActionRepository;
import com.simulador.financiero.services.ActionService;
import com.simulador.financiero.utils.AuthenticatedUserProvider;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/actions")
@AllArgsConstructor
public class ActionController {

    private final ActionService actionService;
    // private final ActionRepository actionRepository;

    @PostMapping("/buy")
    public String buyAction(@RequestParam String ticker,@RequestParam Integer quantity,@RequestParam String accountNumber) {

        AuthenticatedUser authenticatedUser = AuthenticatedUserProvider.getAuthenticatedUser();

        return actionService.buyAction(authenticatedUser.id(), ticker, quantity, accountNumber);
    }

}