package com.simulador.financiero.DTOs.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordResetRequest(
                @Schema(description = "Clave temporal enviada al usuario para autorizar el cambio de contraseña", example = "a1b2c3d4") @NotBlank(message = "Temporal key is required") String temporalKey,
                @Schema(description = "Nueva contraseña, mínimo 8 caracteres", example = "MiClaveSegura1") @NotBlank(message = "New password is required") @Size(min = 8, message = "New password must be at least 8 characters long") String newPassword) {
}
