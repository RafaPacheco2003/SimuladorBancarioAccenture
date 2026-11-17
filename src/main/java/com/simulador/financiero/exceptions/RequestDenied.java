package com.simulador.financiero.exceptions;

public class RequestDenied extends RuntimeException {

    public RequestDenied(String message) {
        super(message);
    }
}
