package com.simulador.financiero.DTOs.request;

import org.antlr.v4.runtime.misc.NotNull;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
  
public record CreateUserRequest(
    
    @NotBlank(message="El nombre es obligatorio") 
    String fullName,
    @NotBlank(message="El curp es obligatorio")
    String curp, 
    @NotBlank(message="El email es obligatorio")
    @Email(message="El correo no tiene un formato válido")
    String email,
    @NotBlank(message="El password es obligatorio")
    @Size(max=16, min=8, message="El password debe tener mínimo 8 caracteres y máximo 16")
    String password,
    String phone) {
   
    

}
