package com.simulador.financiero;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.simulador.financiero.entities.UserEntity;
import com.simulador.financiero.entities.UserStatus;
import com.simulador.financiero.repositories.UserRepository;
import com.simulador.financiero.services.UserService;

@ExtendWith(MockitoExtension.class)
class UserTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void deberiaCrearUsuarioCuandoDatosSeanValidos() {

        UserEntity user = new UserEntity();

        user.setFullName("Carlos Garcia");
        user.setCurp("GARC850101HDFRRL09");
        user.setEmail("carlos@gmail.com");
        user.setPassword("Password123");
        user.setPhone("9991234567");
        user.setSaldo(BigDecimal.ZERO);
        user.setStatus(UserStatus.ACTIVE);

        when(userRepository.existsByCurp("GARC850101HDFRRL09"))
                .thenReturn(false);

        when(userRepository.existsByEmail("carlos@gmail.com"))
                .thenReturn(false);

        when(passwordEncoder.encode("Password123"))
                .thenReturn("passwordCifrada");

        when(userRepository.save(user))
                .thenReturn(user);

        UserEntity resultado = userService.createUser(user);

        assertNotNull(resultado);
        assertEquals("Carlos Garcia", resultado.getFullName());
        assertEquals("GARC850101HDFRRL09", resultado.getCurp());
        assertEquals("carlos@gmail.com", resultado.getEmail());
        assertEquals("passwordCifrada", resultado.getPassword());
        assertEquals(UserStatus.ACTIVE, resultado.getStatus());
    }

    @Test
    void deberiaRechazarRegistroCuandoCurpYaExista() {

        UserEntity user = new UserEntity();

        user.setFullName("Carlos Garcia");
        user.setCurp("GARC850101HDFRRL09");
        user.setEmail("carlos@gmail.com");
        user.setPassword("Password123");

        when(userRepository.existsByCurp("GARC850101HDFRRL09"))
                .thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(user)
        );

        assertEquals(
                "El usuario ya existe",
                exception.getMessage()
        );
    }

    @Test
    void deberiaRechazarRegistroCuandoPasswordSeaCorta() {

        UserEntity user = new UserEntity();

        user.setFullName("Carlos Garcia");
        user.setCurp("GARC850101HDFRRL10");
        user.setEmail("carlos@gmail.com");
        user.setPassword("1234567");

        when(userRepository.existsByCurp("GARC850101HDFRRL10"))
                .thenReturn(false);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(user)
        );

        assertEquals(
                "La contraseña debe tener mínimo 8 caracteres",
                exception.getMessage()
        );
    }

    @Test
    void deberiaEliminarUsuario() {

        userService.deleteUser(1L);

        org.mockito.Mockito.verify(userRepository)
                .deleteById(1L);
    }

    @Test
    void deberiaRechazarRegistroCuandoEmailYaExista() {

        UserEntity user = new UserEntity();

        user.setFullName("Carlos Garcia");
        user.setCurp("GARC850101HDFRRL11");
        user.setEmail("carlos@gmail.com");
        user.setPassword("Password123");

        when(userRepository.existsByCurp("GARC850101HDFRRL11"))
                .thenReturn(false);

        when(userRepository.existsByEmail("carlos@gmail.com"))
                .thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(user)
        );

        assertEquals(
                "El usuario ya existe",
                exception.getMessage()
        );
    }
}