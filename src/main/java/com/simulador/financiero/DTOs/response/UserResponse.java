package com.simulador.financiero.DTOs.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserResponse(
    @NotBlank(message = "El id es obligatorio")
    Long id, 
    @NotBlank(message = "El nombre es obligatorio")
    String fullName, 
    @NotBlank(message = "El curp es obligatorio")
    String curp, 
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    String email, 
    String phone){
}
