package com.simulador.financiero.services;

import org.springframework.stereotype.Service;

import com.simulador.financiero.dtos.request.CreateUserRequest;
import com.simulador.financiero.dtos.response.UserResponse;
public interface IUserService {
    public UserResponse createUser(CreateUserRequest request);
}
