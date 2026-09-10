package com.simulador.financiero.services;

import org.springframework.stereotype.Service;

import com.simulador.financiero.DTOs.request.CreateUserRequest;
import com.simulador.financiero.DTOs.response.UserResponse;


public interface IUserService {
    public UserResponse createUser(CreateUserRequest request);
}
