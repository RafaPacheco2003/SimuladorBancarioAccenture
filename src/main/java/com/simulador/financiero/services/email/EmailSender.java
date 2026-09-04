package com.simulador.financiero.services.email;

public interface EmailSender {

    <T> void send(String to, EmailDefinition<T> email, T data);
}
