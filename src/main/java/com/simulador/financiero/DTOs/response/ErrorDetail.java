package com.simulador.financiero.DTOs.response;

import java.time.LocalDateTime;

public record ErrorDetail ( LocalDateTime timestamp, int status, String error, String message, String path){
    
}
