package com.simulador.financiero.DTOs.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter
@AllArgsConstructor 
@NoArgsConstructor 
//aqui se hace la verificacion del email y el token que se envia al correo para poder cambiar la contraseña
public class VerifyTempKeyRequest {
    private String email;
    private String token;

}
