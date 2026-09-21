package com.simulador.financiero.constants;

public class ExceptionMessageConstants {

    public static final String TOKEN_INVALID_OR_EXPIRED = "Clave temporal inválida, vencida o ya utilizada.";

    public static final String TOO_MANY_TOKEN_REQUESTS = "Máximo de solicitudes, espera 1 minuto";

    public static final String USERNAME_NOT_FOUND = "Usuario no encontrado.";

    public static final String ACCOUNT_NOT_FOUND = "Cuenta no encontrada.";

    public static final String ACCOUNT_NOT_ACTIVE = "La cuenta no está activa o se encuentra bloqueada.";

    public static final String ACCOUNT_NOT_OWNED = "La cuenta de origen no pertenece al usuario autenticado.";

    public static final String SAME_OWNER_TRANSFER = "La cuenta destino no puede pertenecer al mismo usuario que la cuenta origen.";

    public static final String SAME_ACCOUNT_TRANSFER = "La cuenta de origen y destino no pueden ser la misma.";

    public static final String INVALID_AMOUNT = "El monto debe ser mayor a 0.";

    public static final String INSUFFICIENT_BALANCE = "Saldo insuficiente para realizar la transferencia.";

    public static final String UNSUPPORTED_EXCHANGE_RATE = "No existe tipo de cambio configurado para las divisas indicadas.";

    public static final String STOCK_SYMBOL_NOT_FOUND = "No se encontró información para el símbolo bursátil indicado.";

    public static final String EXTERNAL_API_ERROR = "Error al consultar la API externa de cotizaciones.";

    public static final String STOCK_NOT_FOUND = "Acción no encontrada.";

    public static final String NO_ACTIONS_FOR_TICKER = "La cuenta no posee acciones del ticker indicado.";

    public static final String INSUFFICIENT_ACTIONS = "La cuenta no posee suficientes acciones para realizar la venta.";

    private ExceptionMessageConstants() {
        throw new AssertionError("Clase utilitaria, no debe ser instanciada");
    }
}
