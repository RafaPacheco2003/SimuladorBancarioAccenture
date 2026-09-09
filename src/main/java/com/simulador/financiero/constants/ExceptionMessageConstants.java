package com.simulador.financiero.constants;

public class ExceptionMessageConstants {

    public static final String TOKEN_INVALID_OR_EXPIRED = "Clave temporal inválida, vencida o ya utilizada.";

    public static final String TOO_MANY_TOKEN_REQUESTS = "Máximo de solicitudes, espera 1 minuto";

    private ExceptionMessageConstants() {
        throw new AssertionError("Clase utilitaria, no debe ser instanciada");
    }
}
