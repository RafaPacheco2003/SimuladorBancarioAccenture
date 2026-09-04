package com.simulador.financiero.services.email.catalog;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

import org.junit.jupiter.api.Test;

class AccountRecoveryEmailTest {

    private final AccountRecoveryEmail email = new AccountRecoveryEmail();
    private final AccountRecoveryData data =
            new AccountRecoveryData("John", "https://app.simulador.com/recover", "tok-123");

    @Test
    void exposesRecoverAccountTemplate() {
        assertThat(email.template()).isEqualTo("recover-account");
    }

    @Test
    void subjectIsFixed() {
        assertThat(email.subject(data)).isEqualTo("Recupera tu cuenta");
    }

    @Test
    void modelMapsEveryDataFieldWithExpectedKeys() {
        assertThat(email.toModel(data)).containsOnly(
                entry("recipientName", "John"),
                entry("recoveryUrl", "https://app.simulador.com/recover"),
                entry("token", "tok-123"));
    }
}
