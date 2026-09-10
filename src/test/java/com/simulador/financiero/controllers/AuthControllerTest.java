package com.simulador.financiero.controllers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simulador.financiero.DTOs.request.PasswordResetRequest;
import com.simulador.financiero.Exceptions.BadRequestException;
import com.simulador.financiero.constants.ExceptionMessageConstants;
import com.simulador.financiero.services.AuthService;
import com.simulador.financiero.services.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private UserService userService;

    private final String RESET_PASSWORD_ENDPOINT = "/api/v1/auth/reset-password";

    @Test
    void mustBeSuccessIfTheTokenIsValid() throws Exception {
        PasswordResetRequest request = new PasswordResetRequest("valid-token-123", "newPassword");

        doNothing().when(authService).resetPassword(request.newPassword(), request.temporalKey());

        String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(post(RESET_PASSWORD_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk()).andExpect(
                        jsonPath("$.message").value("Contraseña actualizada correctamente. Ya puedes iniciar sesión"));

        verify(authService).resetPassword(request.newPassword(), request.temporalKey());
    }

    @Test
    void mustReturnBadRequestIfTheTokenIsInvalidOrExpired() throws Exception {
        PasswordResetRequest request = new PasswordResetRequest("invalid-token-123", "newPassword");

        doThrow(new BadRequestException(ExceptionMessageConstants.TOKEN_INVALID_OR_EXPIRED))
                .when(authService).resetPassword(request.newPassword(), request.temporalKey());

        String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(post(RESET_PASSWORD_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest()).andExpect(
                        jsonPath("$.message").value(ExceptionMessageConstants.TOKEN_INVALID_OR_EXPIRED));
    }

    @Test
    void mustReturnBadRequestIfTheTemporalKeyIsBlank() throws Exception {
        PasswordResetRequest request = new PasswordResetRequest("", "newPassword");

        String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(post(RESET_PASSWORD_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }

}
