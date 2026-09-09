package com.simulador.financiero.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simulador.financiero.DTOs.request.CreateUserRequest;
import com.simulador.financiero.DTOs.request.LoginRequest;
import com.simulador.financiero.DTOs.response.LoginResponse;
import com.simulador.financiero.DTOs.response.UserResponse;
import com.simulador.financiero.entities.UserEntity;
import com.simulador.financiero.services.AuthService;
import com.simulador.financiero.services.UserService;
import com.simulador.financiero.services.impl.UserServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final UserServiceImpl userServiceImpl;

    public AuthController(AuthService authService, UserServiceImpl userServiceImpl) {
        this.authService = authService;
        this.userServiceImpl = userServiceImpl;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody CreateUserRequest request) {

        UserResponse userResponse = userServiceImpl.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse loginResponse = authService.login(request);
        return ResponseEntity.ok(loginResponse);

    }

}
