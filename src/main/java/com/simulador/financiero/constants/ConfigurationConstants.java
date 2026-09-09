package com.simulador.financiero.constants;

public class ConfigurationConstants {

    public static final int TOKEN_EXPIRATION_MINUTES = 15;

    public static final int TOKEN_REQUEST_COOLDOWN_MINUTES = 1;

    private ConfigurationConstants() {
        throw new AssertionError("Clase utilitaria, no debe ser instanciada");
    }
}
