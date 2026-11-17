package com.simulador.financiero.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    @Schema(description = "Token JWT para enviar en el header Authorization", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
    @Schema(description = "Tipo de token", example = "Bearer")
    private String tipoToken = "Bearer";
    @Schema(description = "Segundos de vigencia del token", example = "3600")
    private long expiraEnSegundos;

    public LoginResponse(String token, long expiraEnSegundos){
        this.token = token;
        this.expiraEnSegundos = expiraEnSegundos;
    }

}
