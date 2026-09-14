package com.simulador.financiero.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;

import com.simulador.financiero.DTOs.request.LoginRequest;
import com.simulador.financiero.DTOs.response.LoginResponse;
import com.simulador.financiero.entities.UserEntity;
import com.simulador.financiero.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.simulador.financiero.Exceptions.BadRequestException;
import com.simulador.financiero.config.security.JwtService;
import com.simulador.financiero.constants.ConfigurationConstants;
import com.simulador.financiero.entities.TempTockenEntity;
import com.simulador.financiero.repositories.TempTokenRepository;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TempTokenRepository tempTokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;
      
    private UserEntity user;

    @BeforeEach
    void setUp() {
        this.user = UserEntity.builder().id(1L).fullName("Juan Pérez").email("juan@example.com")
                .password("oldPassword123").build();
    }

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

    @Test
    public void deberiaRechazarLoginCuandoPasswordSeaIncorrecta() {
        LoginRequest request = new LoginRequest();
        request.setEmail("luis.ake111@gmail.com");
        request.setPassword("wrongpassword");

        when(authenticationManager.authenticate(any()))
            .thenThrow(new BadCredentialsException("Contraseña incorrecta"));

        assertThrows(BadCredentialsException.class, () -> authService.login(request));
    }

    @Test
    public void deberiaRechazarLoginCuandoCorreoNoExista() {
        LoginRequest request = new LoginRequest();
        request.setEmail("nonexistent@gmail.com");
        request.setPassword("123456");

        when(authenticationManager.authenticate(any()))
            .thenThrow(new BadCredentialsException("Credenciales inválidas"));

        assertThrows(BadCredentialsException.class, () -> authService.login(request));
    }

    @Test
    void mustChangePasswordWhenTheTokenIsValid() {
        String token = "valid-token-123";
        String newPassword = "newPassword456";
        String encodedPassword = "encodedPassword789";

        TempTockenEntity tempToken = TempTockenEntity.builder().id(1L).token(token).user(user).build();

        when(tempTokenRepository.findByToken(token)).thenReturn(Optional.of(tempToken));
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);

        authService.resetPassword(newPassword, token);

        assertThat(user.getPassword()).isEqualTo(encodedPassword);
        verify(tempTokenRepository).delete(tempToken);
        verify(userRepository).save(user);
    }

    @Test
    void mustThrowExceptionWhenTheTokenHasExpired() {
        String token = "invalid-token-123";
        String newPassword = "newPassword";

        TempTockenEntity tempToken = TempTockenEntity.builder().id(1L).token(token).user(user)
                .createdAt(LocalDateTime.now().minusMinutes((long) ConfigurationConstants.TOKEN_EXPIRATION_MINUTES + 3L))
                .build();

        when(tempTokenRepository.findByToken(token)).thenReturn(Optional.of(tempToken));

        assertThrows(BadRequestException.class, () -> authService.resetPassword(newPassword, token));
        verify(tempTokenRepository).delete(tempToken);
    }

    @Test
    void mustThrowExceptionWhenTheTokenDoesNotExist() {
        String token = "non-existent-token";
        String newPassword = "newPassword";

        when(tempTokenRepository.findByToken(token)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> authService.resetPassword(newPassword, token));
    }

}
