package com.simulador.financiero.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez López")
    @NotBlank(message = "El nombre es obligatorio")
    String fullName,
    @Schema(description = "CURP del usuario", example = "PELJ900101HDFRPN09")
    @NotBlank(message = "El curp es obligatorio")
    String curp,
    @Schema(description = "Correo electrónico del usuario", example = "juan.perez@example.com")
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    String email,
    @Schema(description = "Contraseña, mínimo 8 caracteres", example = "MiClaveSegura1")
    @NotBlank(message = "El password es obligatorio")
    @Size(min = 8, message = "La contraseña debe tener mínimo 8 caracteres")
    String password,
    @Schema(description = "Teléfono de contacto (opcional)", example = "5512345678")
    String phone) {
}
