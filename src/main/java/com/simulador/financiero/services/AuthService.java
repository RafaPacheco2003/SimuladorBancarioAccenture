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

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.simulador.financiero.DTOs.request.LoginRequest;
import com.simulador.financiero.DTOs.response.LoginResponse;
import com.simulador.financiero.repositories.UserRepository;

@Service
@AllArgsConstructor 
public class AuthService {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
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

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()));

        var user = userRepository.findByEmail(request.getEmail());

        String token = jwtService.generateToken(user.get());
        return new LoginResponse(token, jwtService.getExpirationTime());

    }
}
