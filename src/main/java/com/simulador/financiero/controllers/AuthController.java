package com.simulador.financiero.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simulador.financiero.DTOs.request.PasswordResetRequest;
import com.simulador.financiero.DTOs.response.ErrorDetail;
import com.simulador.financiero.DTOs.response.PasswordResetResponse;
import com.simulador.financiero.services.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/v1/auth")
@AllArgsConstructor
@Tag(name = "Auth", description = "Autenticación y recuperación de cuenta")
public class AuthController {

    private final AuthService authService;

    @PostMapping("reset-password")
    @Operation(summary = "Cambiar contraseña con clave temporal", description = "Valida la clave temporal y actualiza la contraseña del usuario.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contraseña actualizada", content = @Content(schema = @Schema(implementation = PasswordResetResponse.class))),
            @ApiResponse(responseCode = "400", description = "Clave temporal inválida, vencida o ya utilizada, o datos de entrada inválidos", content = @Content(schema = @Schema(implementation = ErrorDetail.class)))
    })
    public PasswordResetResponse resetPassword(@RequestBody @Valid PasswordResetRequest request) {

        authService.resetPassword(request.newPassword(), request.temporalKey());

        return new PasswordResetResponse("Contraseña actualizada correctamente. Ya puedes iniciar sesión");
    }

}
