package com.simulador.financiero.services.email;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simulador.financiero.Exceptions.ResourceNotFoundException;
import com.simulador.financiero.entities.TempTockenEntity;
import com.simulador.financiero.entities.UserEntity;
import com.simulador.financiero.repositories.TempTokenRepository;
import com.simulador.financiero.repositories.UserRepository;
import com.simulador.financiero.services.email.catalog.AccountRecoveryData;
import com.simulador.financiero.services.email.catalog.AccountRecoveryEmail;
import com.simulador.financiero.utils.TockenGenerate;

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

        String verification = tockenGenerate.generateTempKey();
        LocalDateTime now = LocalDateTime.now();

        if (existing.isPresent()) {

            TempTockenEntity previous = existing.get();

            previous.setToken(verification);
            previous.setCreatedAt(now);

            tempTokenRepository.save(previous);

        } else {

            tempTokenRepository.save(
                    TempTockenEntity.builder()
                            .token(verification)
                            .user(user)
                            .createdAt(now)
                            .build()
            );
        }

        AccountRecoveryData data = new AccountRecoveryData(
                user.getFullName(),
                RECOVERY_URL,
                verification
        );
        /*
        emailSender.send(
                email,
                new AccountRecoveryEmail(),
                data
        );
         */
        return true;
    }
}