package com.simulador.financiero.DTOs.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
    @NotBlank(message = "El nombre es obligatorio")
    String fullName, 
    @NotBlank(message = "El curp es obligatorio")
    String curp, 
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    String email, 
    @NotBlank(message = "El password es obligatorio")
    @Size(min = 8, message = "La contraseña debe tener mínimo 8 caracteres")
    String password, 
    String phone) {
}
