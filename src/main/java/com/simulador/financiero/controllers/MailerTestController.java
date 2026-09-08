package com.simulador.financiero.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simulador.financiero.services.email.EmailSender;
import com.simulador.financiero.services.email.catalog.AccountRecoveryData;
import com.simulador.financiero.services.email.catalog.AccountRecoveryEmail;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/test/mailer")
public class MailerTestController {

    EmailSender emailSender;

    public MailerTestController(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    @GetMapping
    public String testMail() {
        AccountRecoveryData data = new AccountRecoveryData("John Doe", "http://localhost:8080/", "tokenExample");
        AccountRecoveryEmail email = new AccountRecoveryEmail();
        emailSender.send("john.doe@example.com", email, data);
        return "Test mail sent successfully!";
    }

}

