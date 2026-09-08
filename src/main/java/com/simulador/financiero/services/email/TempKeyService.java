package com.simulador.financiero.services.email;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.simulador.financiero.Exceptions.RequestDenied;
import com.simulador.financiero.Exceptions.ResourceNotFoundException;
import com.simulador.financiero.entities.TempTockenEntity;
import com.simulador.financiero.entities.UserEntity;
import com.simulador.financiero.repositories.TempTokenRepository;
import com.simulador.financiero.repositories.UserRepository;
import com.simulador.financiero.services.email.catalog.AccountRecoveryData;
import com.simulador.financiero.services.email.catalog.AccountRecoveryEmail;
import com.simulador.financiero.utils.TockenGenerate;
import com.simulador.financiero.validate.ValidateTemp;
import com.simulador.financiero.Exceptions.RequestDenied;

@Service
public class TempKeyService {

private final Map<String, LocalDateTime> lastRequest=new HashMap();
private final EmailSender emailSender;
private final UserRepository userRepository;
private final TockenGenerate tockenGenerate;
private final TempTokenRepository tempTokenRepository;
private final ValidateTemp validateTemp;
private final String RECOVERY_URL;

public TempKeyService(
        EmailSender emailSender,
        UserRepository userRepository,
        TockenGenerate tockenGenerate,
        TempTokenRepository tempTokenRepository,
        ValidateTemp validateTemp,
        @Value("${app.mail.recovery.password.url}") String recoveryUrl) {

    this.emailSender = emailSender;
    this.userRepository = userRepository;
    this.tockenGenerate = tockenGenerate;
    this.tempTokenRepository = tempTokenRepository;
    this.validateTemp = validateTemp;
    this.RECOVERY_URL = recoveryUrl;
}

public String generateTempKey(Integer tempkey) {
    return tempkey.toString();
}

public boolean validateUser(String email) {

    Optional<UserEntity> user = userRepository.findByEmail(email);

    if (user.isEmpty()) {
        throw new ResourceNotFoundException(
                "Usuario no encontrado con el correo: " + email
        );
    }

    String verification = tockenGenerate.generateTempKey();

    LocalDateTime expiration =
            LocalDateTime.now().plusMinutes(15);

    TempTockenEntity tempToken = new TempTockenEntity();

    tempToken.setEmail(email);
    tempToken.setToken(verification);
    tempToken.setExpiration(expiration);

    tempTokenRepository.save(tempToken);

    AccountRecoveryData data =
            new AccountRecoveryData(
                    user.get().getFullName(),
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

public boolean verifyTempKey(String email, String token) {

    Optional<TempTockenEntity> tempToken =
            tempTokenRepository.findByEmailAndToken(email, token);

    if (tempToken.isEmpty()) {
        return false;
    }

    return LocalDateTime.now()
            .isBefore(tempToken.get().getExpiration());
}

public String validate(String email, String token) {
    return validateTemp.validate(email, token);
}

//metodo cliente que solicita una clave temporal en menos de un min y lo rechaza
public String withoutKey(String email){
        LocalDateTime last = lastRequest.get(email);
        LocalDateTime now = LocalDateTime.now();

        if (last != null &&
           last.plusMinutes(1).isAfter(now)) {

            throw new RequestDenied(
                "Máximo de solicitud, espera 1 minuto"
            );
        }

        lastRequest.put(email, now);
        return "Solicitud terminada";
        }
}
