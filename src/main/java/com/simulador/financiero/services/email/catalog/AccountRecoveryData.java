package com.simulador.financiero.services.email.catalog;


public record AccountRecoveryData(String recipientName, String recoveryUrl, String token) {
}
