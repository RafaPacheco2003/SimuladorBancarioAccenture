package com.simulador.financiero.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simulador.financiero.DTOs.request.LoginRequest;
import com.simulador.financiero.DTOs.response.LoginResponse;
import com.simulador.financiero.entities.UserEntity;
import com.simulador.financiero.entities.UserResponse;
import com.simulador.financiero.services.AuthService;
import com.simulador.financiero.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService){
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody UserEntity user) {

        UserEntity usuarioCreado = userService.createUser(user);

        UserResponse response = new UserResponse(
                usuarioCreado.getId(),
                usuarioCreado.getFullName(),
                usuarioCreado.getEmail(),
                usuarioCreado.getStatus().name(),
                usuarioCreado.getCreatedAt()
        );
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse loginResponse = authService.login(request);
        return ResponseEntity.ok(loginResponse);
        
    }
    
}
