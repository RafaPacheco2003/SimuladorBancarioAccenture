package com.simulador.financiero.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simulador.financiero.DTOs.request.TempKeyRequest;
import com.simulador.financiero.services.email.EmailSender;
import com.simulador.financiero.services.email.TempKeyService;
import com.simulador.financiero.services.email.catalog.AccountRecoveryData;
import com.simulador.financiero.services.email.catalog.AccountRecoveryEmail;

@RestController
@RequestMapping("/api/v1/auth")
public class MailerTestController {

    private final TempKeyService tempKeyService;
    private final EmailSender emailSender;

    public MailerTestController(
            TempKeyService tempKeyService,
            EmailSender emailSender) {

        this.tempKeyService = tempKeyService;
        this.emailSender = emailSender;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @RequestBody TempKeyRequest request) {

        tempKeyService.validateUser(request.getEmail());

        return ResponseEntity.ok("Token enviado correctamente");
    }

    @GetMapping("/test-mail")
    public ResponseEntity<String> testMail() {

        AccountRecoveryData data = new AccountRecoveryData(
                "John Doe",
                "http://localhost:8080/",
                "tokenExample"
        );

        AccountRecoveryEmail email = new AccountRecoveryEmail();

        emailSender.send(
                "john.doe@example.com",
                email,
                data
        );

        return ResponseEntity.ok("Test mail sent successfully!");
    }
}