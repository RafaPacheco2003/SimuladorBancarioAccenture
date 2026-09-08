package com.simulador.financiero.validators;

import java.time.LocalDateTime;

import com.simulador.financiero.Exceptions.BadRequestException;
import com.simulador.financiero.entities.TokenStatus;
import com.simulador.financiero.entities.UserTokenEntity;

public class TokenValidator {

    public static final String INVALID_TOKEN_MESSAGE = "Clave temporal inválida, vencida o ya utilizada.";

    private static final int EXPIRATION_MINUTES = 15;

    public static void validate(UserTokenEntity userToken) {

        if (!isActive(userToken.getStatus()) || isExpired(userToken.getCreatedAt())) {
            throw new BadRequestException(INVALID_TOKEN_MESSAGE);
        }
    }

    private static boolean isActive(TokenStatus status) {
        return status.equals(TokenStatus.ACTIVE);
    }

    private static boolean isExpired(LocalDateTime createdAt) {
        LocalDateTime expirationTime = createdAt.plusMinutes(EXPIRATION_MINUTES);
        return LocalDateTime.now().isAfter(expirationTime);
    }
}
