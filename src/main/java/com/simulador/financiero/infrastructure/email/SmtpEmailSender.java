package com.simulador.financiero.infrastructure.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.simulador.financiero.services.email.EmailDefinition;
import com.simulador.financiero.services.email.EmailSender;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class SmtpEmailSender implements EmailSender {

    private final JavaMailSender javaMailSender;
    private final EmailTemplateRenderer renderer;
    private final String fromAddress;

    public SmtpEmailSender(JavaMailSender javaMailSender,
            EmailTemplateRenderer renderer,
            @Value("${app.mail.from}") String fromAddress) {
        this.javaMailSender = javaMailSender;
        this.renderer = renderer;
        this.fromAddress = fromAddress;
    }

    @Override
    public <T> void send(String to, EmailDefinition<T> email, T data) {
        String body = renderer.render(email.template(), email.toModel(data));
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setFrom(fromAddress);
            helper.setTo(to);
            helper.setSubject(email.subject(data));
            helper.setText(body, true);
            javaMailSender.send(message);
        } catch (MessagingException | MailException exception) {
            throw new IllegalStateException("Unable to send email", exception);
        }
    }
}
