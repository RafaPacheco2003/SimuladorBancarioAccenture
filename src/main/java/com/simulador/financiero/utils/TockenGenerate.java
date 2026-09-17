package com.simulador.financiero.utils;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

@Component
public class TockenGenerate {

    private static final String CHARACTERS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int TOKEN_LENGTH = 36;

    private final SecureRandom random = new SecureRandom();

    public String generateTempKey() {
        StringBuilder token = new StringBuilder(TOKEN_LENGTH);

        for (int i = 0; i < TOKEN_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            token.append(CHARACTERS.charAt(index));
        }

        return token.toString();
    }
}

