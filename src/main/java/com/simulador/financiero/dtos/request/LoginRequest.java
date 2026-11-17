package com.simulador.financiero.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @Schema(description = "Correo electrónico del usuario", example = "juan.perez@example.com")
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato válido")
    private String email;

    @Schema(description = "Contraseña del usuario", example = "MiClaveSegura1")
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;


}
