package com.simulador.financiero.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simulador.financiero.entities.UserEntity;
import com.simulador.financiero.entities.UserResponse;
import com.simulador.financiero.repositories.UserRepository;
import com.simulador.financiero.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {

        return userService.getAllUsers()
                .stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getFullName(),
                        user.getEmail(),
                        user.getStatus().name(),
                        user.getCreatedAt()
                ))
                .toList();
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id) {

        UserEntity user = userService.getUserById(id);

        if (user == null) {
            return null;
        }

        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getStatus().name(),
                user.getCreatedAt()
        );
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

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @RequestBody UserEntity user) {

        UserEntity usuarioActualizado =
                userService.updateUser(id, user);

        if (usuarioActualizado == null) {
            return ResponseEntity.notFound().build();
        }

        UserResponse response = new UserResponse(
                usuarioActualizado.getId(),
                usuarioActualizado.getFullName(),
                usuarioActualizado.getEmail(),
                usuarioActualizado.getStatus().name(),
                usuarioActualizado.getCreatedAt()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
