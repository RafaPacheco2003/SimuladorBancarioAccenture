package com.simulador.financiero.services;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.simulador.financiero.entities.UserEntity;
import com.simulador.financiero.entities.UserStatus;
import com.simulador.financiero.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    //me quede en la parte del service mapper, createuserrequest y entityuser
    public UserEntity createUser(UserEntity user) {
        
        // Cifrar contraseña
        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        // Todo usuario nuevo comienza ACTIVO
        user.setStatus(UserStatus.ACTIVE);

        return userRepository.save(user);
    }

    // READ ALL
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    // READ POR ID
    public UserEntity getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // UPDATE
    public UserEntity updateUser(Long id, UserEntity user) {

        UserEntity existente =
                userRepository.findById(id).orElse(null);

        if (existente == null) {
            return null;
        }

        if (user.getCurp() != null &&
                !user.getCurp().equals(existente.getCurp()) &&
                userRepository.existsByCurp(user.getCurp())) {

            throw new IllegalArgumentException("El CURP ya existe");
        }

        if (user.getEmail() != null &&
                !user.getEmail().equals(existente.getEmail()) &&
                userRepository.existsByEmail(user.getEmail())) {

            throw new IllegalArgumentException("El usuario ya existe");
        }

        if (user.getFullName() != null) {
            existente.setFullName(user.getFullName());
        }

        if (user.getCurp() != null) {
            existente.setCurp(user.getCurp());
        }

        if (user.getEmail() != null) {
            existente.setEmail(user.getEmail());
        }

        if (user.getPassword() != null) {
            existente.setPassword(
                    passwordEncoder.encode(user.getPassword())
            );
        }

        if (user.getPhone() != null) {
            existente.setPhone(user.getPhone());
        }

        if (user.getSaldo() != null) {
            existente.setSaldo(user.getSaldo());
        }

        if (user.getStatus() != null) {
            existente.setStatus(user.getStatus());
        }

        return userRepository.save(existente);
    }

    // DELETE
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
