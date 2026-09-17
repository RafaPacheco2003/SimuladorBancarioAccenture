package com.simulador.financiero.services.email;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simulador.financiero.Exceptions.RequestDenied;
import com.simulador.financiero.Exceptions.ResourceNotFoundException;
import com.simulador.financiero.constants.ExceptionMessageConstants;
import com.simulador.financiero.entities.TempTockenEntity;
import com.simulador.financiero.entities.UserEntity;
import com.simulador.financiero.repositories.TempTokenRepository;
import com.simulador.financiero.repositories.UserRepository;
import com.simulador.financiero.services.email.catalog.AccountRecoveryData;
import com.simulador.financiero.services.email.catalog.AccountRecoveryEmail;
import com.simulador.financiero.utils.TockenGenerate;
import com.simulador.financiero.validators.ResetTokenValidator;

@Service
public class TempKeyService {

    private final EmailSender emailSender;
    private final UserRepository userRepository;
    private final TockenGenerate tockenGenerate;
    private final TempTokenRepository tempTokenRepository;
    private final String RECOVERY_URL;

    public TempKeyService(
            EmailSender emailSender,
            UserRepository userRepository,
            TockenGenerate tockenGenerate,
            TempTokenRepository tempTokenRepository,
            @Value("${app.mail.recovery.password.url}") String recoveryUrl) {

        this.emailSender = emailSender;
        this.userRepository = userRepository;
        this.tockenGenerate = tockenGenerate;
        this.tempTokenRepository = tempTokenRepository;
        this.RECOVERY_URL = recoveryUrl;
    }

    @Transactional
    public boolean validateUser(String email) {

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con el correo: " + email));

        Optional<TempTockenEntity> existing = tempTokenRepository.findByUser(user);

        if (existing.isPresent()) {
            TempTockenEntity previous = existing.get();

            if (ResetTokenValidator.isExpired(previous.getCreatedAt())) {
                // Ya existe un token vencido: se elimina y se continúa para crear uno nuevo
                tempTokenRepository.delete(previous);
            } else if (ResetTokenValidator.isWithinCooldown(previous.getCreatedAt())) {
                // Token aún vigente y solicitado hace menos de 1 minuto: se rechaza
                throw new RequestDenied(ExceptionMessageConstants.TOO_MANY_TOKEN_REQUESTS);
            } else {
                // Token aún vigente pero fuera del enfriamiento: se reemplaza por uno nuevo
                tempTokenRepository.delete(previous);
            }
        }

        String verification = tockenGenerate.generateTempKey();

        tempTokenRepository.save(TempTockenEntity.builder()
                .token(verification)
                .user(user)
                .build());

        AccountRecoveryData data = new AccountRecoveryData(
                user.getFullName(),
                RECOVERY_URL,
                verification);

        emailSender.send(
                email,
                new AccountRecoveryEmail(),
                data);

        return true;
    }
}
