package com.simulador.financiero.validators;

import java.time.LocalDateTime;

import com.simulador.financiero.constants.ConfigurationConstants;

public class ResetTokenValidator {

    public static boolean isExpired(LocalDateTime createdAt) {
        LocalDateTime expirationTime = createdAt.plusMinutes(ConfigurationConstants.TOKEN_EXPIRATION_MINUTES);
        return LocalDateTime.now().isAfter(expirationTime);
    }

    private ResetTokenValidator() {
        throw new AssertionError("Clase utilitaria, no debe ser instanciada");
    }
}
