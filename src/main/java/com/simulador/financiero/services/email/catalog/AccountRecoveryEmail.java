package com.simulador.financiero.services.email.catalog;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.simulador.financiero.services.email.EmailDefinition;

@Component
public class AccountRecoveryEmail implements EmailDefinition<AccountRecoveryData> {

    private static final String TEMPLATE = "recover-account";

    @Override
    public String template() {
        return TEMPLATE;
    }

    @Override
    public String subject(AccountRecoveryData data) {
        return "Recupera tu cuenta";
    }

    @Override
    public Map<String, Object> toModel(AccountRecoveryData data) {
        return Map.of(
                "recipientName", data.recipientName(),
                "recoveryUrl", data.recoveryUrl(),
                "token", data.token());
    }
}
