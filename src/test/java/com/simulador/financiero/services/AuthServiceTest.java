package com.simulador.financiero.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;

import com.simulador.financiero.DTOs.request.LoginRequest;
import com.simulador.financiero.DTOs.response.LoginResponse;
import com.simulador.financiero.entities.UserEntity;
import com.simulador.financiero.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    public void deberiaEntregarTokenCuandoCredencialesSeanCorrectas() {
        LoginRequest request = new LoginRequest();
        request.setEmail("luis.ake111@gmail.com");
        request.setPassword("password123");

        UserEntity user = new UserEntity();
        user.setId(2L);
        user.setEmail("luis.ake111@gmail.com");
        user.setFullName("Luis Gerardo");

        LoginResponse esperado = new LoginResponse("token-test", 86400L);

        when(userRepository.findByEmail("luis.ake111@gmail.com"))
            .thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("token-test");
        when(jwtService.getExpirationTime()).thenReturn(86400L);

        LoginResponse resultado = authService.login(request);

        assertEquals(esperado.getToken(), resultado.getToken());
        assertEquals(esperado.getExpiraEnSegundos(), resultado.getExpiraEnSegundos());
    }
}
