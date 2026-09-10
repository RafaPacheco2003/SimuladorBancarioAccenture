package com.simulador.financiero.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simulador.financiero.Exceptions.BadRequestException;
import com.simulador.financiero.constants.ExceptionMessageConstants;
import com.simulador.financiero.entities.UserEntity;
import com.simulador.financiero.entities.TempTockenEntity;
import com.simulador.financiero.repositories.UserRepository;
import com.simulador.financiero.repositories.TempTokenRepository;
import com.simulador.financiero.validators.ResetTokenValidator;

import lombok.AllArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.simulador.financiero.DTOs.request.LoginRequest;
import com.simulador.financiero.DTOs.response.LoginResponse;

@Service
@AllArgsConstructor 
public class AuthService {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final TempTokenRepository tempTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void resetPassword(String newPassword, String temporalKey) {

        TempTockenEntity tempToken = tempTokenRepository.findByToken(temporalKey)
                .orElseThrow(() -> new BadRequestException(ExceptionMessageConstants.TOKEN_INVALID_OR_EXPIRED));

        if (ResetTokenValidator.isExpired(tempToken.getCreatedAt())){
            tempTokenRepository.delete(tempToken);
            throw new BadRequestException(ExceptionMessageConstants.TOKEN_INVALID_OR_EXPIRED);
        }

        UserEntity user = tempToken.getUser();

        tempTokenRepository.delete(tempToken);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
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
