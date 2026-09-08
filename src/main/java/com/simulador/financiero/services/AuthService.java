package com.simulador.financiero.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.simulador.financiero.Exceptions.BadRequestException;
import com.simulador.financiero.entities.TokenStatus;
import com.simulador.financiero.entities.UserEntity;
import com.simulador.financiero.entities.UserTokenEntity;
import com.simulador.financiero.repositories.UserTokenRepository;
import com.simulador.financiero.validators.TokenValidator;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserTokenRepository userTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void resetPassword(String newPassword, String temporalKey) {

        UserTokenEntity userToken = userTokenRepository.findByToken(temporalKey)
                .orElseThrow(() -> new BadRequestException(TokenValidator.INVALID_TOKEN_MESSAGE));

        TokenValidator.validate(userToken);

        UserEntity user = userToken.getUser();

        userToken.setStatus(TokenStatus.USED);
        user.setPassword(passwordEncoder.encode(newPassword));
    }

}
