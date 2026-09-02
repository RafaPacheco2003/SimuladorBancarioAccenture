package com.simulador.financiero.DTOs.response;

import java.time.LocalDateTime;

public class ApiResponse <T>{
   
    
    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public ApiResponse(
            boolean success,
            String message,
            T data,
            LocalDateTime timestamp) {

        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = timestamp;
    }


}
