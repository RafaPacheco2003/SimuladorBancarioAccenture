package com.simulador.financiero.DTOs.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter 
public class LoginResponse {
    private String token;
    private String tipoToken = "Bearer";
    private long expiraEnSegundos;

    public LoginResponse(String token, long expiraEnSegundos){
        this.token = token;
        this.expiraEnSegundos = expiraEnSegundos;
    }

}
