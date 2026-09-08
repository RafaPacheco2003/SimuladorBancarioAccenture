package com.simulador.financiero.validate;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.simulador.financiero.entities.TempTockenEntity;
import com.simulador.financiero.repositories.TempTokenRepository;

@Service
public class ValidateTemp {

private final TempTokenRepository tempTokenRepository;

public ValidateTemp(TempTokenRepository tempTokenRepository) {
    this.tempTokenRepository = tempTokenRepository;
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

    boolean valid = verifyTempKey(email, token);

    if (!valid) {
        return "El token es inválido o ha expirado";
    }

    return "Token válido";
}

}
