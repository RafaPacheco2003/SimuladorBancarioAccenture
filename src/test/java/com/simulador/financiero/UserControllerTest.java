/*package com.simulador.financiero;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simulador.financiero.controllers.GlobalException;
import com.simulador.financiero.controllers.UserController;
import com.simulador.financiero.entities.UserEntity;
import com.simulador.financiero.entities.UserStatus;
import com.simulador.financiero.services.UserService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalException.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private UserEntity datosValidos() {
        UserEntity user = new UserEntity();
        user.setFullName("Carlos Garcia");
        user.setCurp("GARC850101HDFRRL09");
        user.setEmail("carlos@gmail.com");
        user.setPassword("Password123");
        user.setPhone("9991234567");
        return user;
    }

    @Test
    @DisplayName("POST /api/v1/auth/register con datos válidos -> 201 Created")
    void deberiaRetornar201CuandoDatosSonValidos() throws Exception {
        UserEntity request = datosValidos();

        UserEntity guardado = datosValidos();
        guardado.setId(1L);
        guardado.setPassword("passwordCifrada");
        guardado.setSaldo(BigDecimal.ZERO);
        guardado.setStatus(UserStatus.ACTIVE);
        guardado.setCreatedAt(LocalDateTime.now());

        when(userService.createUser(any(UserEntity.class))).thenReturn(guardado);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("carlos@gmail.com"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    @DisplayName("POST /api/v1/auth/register con CURP o email duplicado -> 409 Conflict")
    void deberiaRetornar409CuandoUsuarioYaExiste() throws Exception {
        UserEntity request = datosValidos();

        when(userService.createUser(any(UserEntity.class)))
                .thenThrow(new IllegalArgumentException("El usuario ya existe"));

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(content().string("El usuario ya existe"));
    }

    @Test
    @DisplayName("POST /api/v1/auth/register con password < 8 caracteres -> 400 Bad Request")
    void deberiaRetornar400CuandoPasswordEsCorta() throws Exception {
        UserEntity request = datosValidos();
        request.setPassword("1234567");

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errores.password").exists());

        verifyNoInteractions(userService);
    }

    @Test
    @DisplayName("POST /api/v1/auth/register con email de formato inválido -> 400 Bad Request")
    void deberiaRetornar400CuandoEmailTieneFormatoInvalido() throws Exception {
        UserEntity request = datosValidos();
        request.setEmail("correo-sin-formato-valido");

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errores.email").exists());

        verifyNoInteractions(userService);
    }
}
*/