package com.simulador.financiero.infrastructure.email;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;

import com.simulador.financiero.services.email.catalog.AccountRecoveryData;
import com.simulador.financiero.services.email.catalog.AccountRecoveryEmail;

import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;

@ExtendWith(MockitoExtension.class)
class SmtpEmailSenderTest {

    private static final String FROM = "no-reply@simulador.com";
    private static final String TO = "john.doe@example.com";
    private static final String RENDERED_BODY = "<html><body>hola</body></html>";

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private EmailTemplateRenderer renderer;

    private SmtpEmailSender emailSender;

    private final AccountRecoveryEmail definition = new AccountRecoveryEmail();
    private final AccountRecoveryData data =
            new AccountRecoveryData("John", "https://app.simulador.com/recover", "tok-123");

    @BeforeEach
    void setUp() {
        emailSender = new SmtpEmailSender(javaMailSender, renderer, FROM);
    }

    @Test
    void rendersTemplateWithDefinitionModelAndSendsHtmlMessage() throws Exception {
        when(renderer.render(eq(definition.template()), anyMap())).thenReturn(RENDERED_BODY);
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));

        emailSender.send(TO, definition, data);

        verify(renderer).render(definition.template(), definition.toModel(data));

        ArgumentCaptor<MimeMessage> captor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(javaMailSender).send(captor.capture());
        MimeMessage sent = captor.getValue();

        assertThat(sent.getSubject()).isEqualTo(definition.subject(data));
        assertThat(sent.getFrom()[0].toString()).isEqualTo(FROM);
        assertThat(sent.getRecipients(Message.RecipientType.TO)[0].toString()).isEqualTo(TO);
        assertThat(sent.getContentType()).contains("text/html");
        assertThat(sent.getContent().toString()).isEqualTo(RENDERED_BODY);
    }

    @Test
    void wrapsMailExceptionIntoIllegalStateException() {
        when(renderer.render(eq(definition.template()), anyMap())).thenReturn(RENDERED_BODY);
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        MailSendException cause = new MailSendException("smtp down");
        doThrow(cause).when(javaMailSender).send(any(MimeMessage.class));

        assertThatThrownBy(() -> emailSender.send(TO, definition, data))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Unable to send email")
                .hasCause(cause);
    }
}
