package com.simulador.financiero.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class TempKeyRequest{

    @Schema(description = "Correo electrónico registrado al que se envía la clave temporal", example = "juan.perez@example.com")
    @NotBlank
    @Email
    private String email;

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email=email;
    }
}
