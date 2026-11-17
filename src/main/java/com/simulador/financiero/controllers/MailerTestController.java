package com.simulador.financiero.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simulador.financiero.DTOs.request.TempKeyRequest;
import com.simulador.financiero.services.email.TempKeyService;

@RestController
@RequestMapping("/api/v1/auth")
public class MailerTestController {

    private final TempKeyService tempKeyService;

    public MailerTestController(TempKeyService tempKeyService) {

        this.tempKeyService = tempKeyService;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @RequestBody TempKeyRequest request) {

        tempKeyService.validateUser(request.getEmail());

        return ResponseEntity.ok("Token enviado correctamente");
    }
}
