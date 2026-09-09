package com.simulador.financiero.validators;

import java.time.LocalDateTime;

import com.simulador.financiero.constants.ConfigurationConstants;

public class ResetTokenValidator {

    public static boolean isExpired(LocalDateTime createdAt) {
        LocalDateTime expirationTime = createdAt.plusMinutes(ConfigurationConstants.TOKEN_EXPIRATION_MINUTES);
        return LocalDateTime.now().isAfter(expirationTime);
    }

    public static boolean isWithinCooldown(LocalDateTime createdAt) {
        LocalDateTime cooldownEnd = createdAt.plusMinutes(ConfigurationConstants.TOKEN_REQUEST_COOLDOWN_MINUTES);
        return LocalDateTime.now().isBefore(cooldownEnd);
    }

    private ResetTokenValidator() {
        throw new AssertionError("Clase utilitaria, no debe ser instanciada");
    }
}
