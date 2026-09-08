package com.simulador.financiero.DTOs.response;

import java.time.LocalDateTime;

import com.simulador.financiero.DTOs.request.CreateUserRequest;
import com.simulador.financiero.entities.UserEntity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public record UserResponse (

    
    Long id,
    String fullName,
    String curp, 
    String email,
    String phone){

}



