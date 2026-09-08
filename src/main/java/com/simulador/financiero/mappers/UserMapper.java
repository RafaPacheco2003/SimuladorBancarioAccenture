package com.simulador.financiero.mappers;

import javax.swing.text.html.parser.Entity;

import org.springframework.stereotype.Component;

import com.simulador.financiero.DTOs.request.CreateUserRequest;
import com.simulador.financiero.DTOs.response.UserResponse;
import com.simulador.financiero.entities.UserEntity;
@Component 
public class UserMapper {

    public UserEntity toEntity(CreateUserRequest request){
        return new UserEntity(request.fullName(),request.curp(), request.email(),request.password(), request.phone());
    }

    public UserResponse toResponse(UserEntity entity){
        return new UserResponse(entity.getId(), entity.getFullName(), entity.getCurp(), entity.getEmail(), entity.getPhone());
    }
}
