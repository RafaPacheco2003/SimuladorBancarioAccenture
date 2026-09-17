package com.simulador.financiero.services.email;

import java.util.Map;

public interface EmailDefinition<T> {

    String template();

    String subject(T data);

    Map<String, Object> toModel(T data);
}
