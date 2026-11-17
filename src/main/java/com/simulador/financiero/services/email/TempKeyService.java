package com.simulador.financiero.services.email;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simulador.financiero.exceptions.ResourceNotFoundException;
import com.simulador.financiero.entities.TempTokenEntity;
import com.simulador.financiero.entities.UserEntity;
import com.simulador.financiero.repositories.TempTokenRepository;
import com.simulador.financiero.repositories.UserRepository;
import com.simulador.financiero.services.email.catalog.AccountRecoveryData;
import com.simulador.financiero.services.email.catalog.AccountRecoveryEmail;
import com.simulador.financiero.utils.TokenGenerator;

@Service
public class TempKeyService {

    private final EmailSender emailSender;
    private final UserRepository userRepository;
    private final TokenGenerator tokenGenerator;
    private final TempTokenRepository tempTokenRepository;
    private final String RECOVERY_URL;

    public TempKeyService(
            EmailSender emailSender,
            UserRepository userRepository,
            TokenGenerator tokenGenerator,
            TempTokenRepository tempTokenRepository,
            @Value("${app.mail.recovery.password.url}") String recoveryUrl) {

        this.emailSender = emailSender;
        this.userRepository = userRepository;
        this.tokenGenerator = tokenGenerator;
        this.tempTokenRepository = tempTokenRepository;
        this.RECOVERY_URL = recoveryUrl;
    }

    @Transactional
    public boolean validateUser(String email) {

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con el correo: " + email));

        Optional<TempTokenEntity> existing = tempTokenRepository.findByUser(user);

        String verification = tokenGenerator.generateTempKey();
        LocalDateTime now = LocalDateTime.now();

        if (existing.isPresent()) {

            TempTokenEntity previous = existing.get();

            previous.setToken(verification);
            previous.setCreatedAt(now);

            tempTokenRepository.save(previous);

        } else {

            tempTokenRepository.save(
                    TempTokenEntity.builder()
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
        emailSender.send(
                email,
                new AccountRecoveryEmail(),
                data
        );

        return true;
    }
}