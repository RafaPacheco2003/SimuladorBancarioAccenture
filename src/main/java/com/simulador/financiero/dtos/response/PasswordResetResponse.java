package com.simulador.financiero.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record PasswordResetResponse(
        @Schema(description = "Mensaje de confirmación", example = "Contraseña actualizada correctamente. Ya puedes iniciar sesión") String message) {

}
