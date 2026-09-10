package com.simulador.financiero.services.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.simulador.financiero.DTOs.request.CreateUserRequest;
import com.simulador.financiero.DTOs.response.UserResponse;
import com.simulador.financiero.entities.UserEntity;
import com.simulador.financiero.mappers.UserMapper;
import com.simulador.financiero.repositories.UserRepository;
import com.simulador.financiero.services.IUserService;

@Service
public class UserServiceImpl implements IUserService{
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserServiceImpl(UserMapper userMapper, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override 
    public UserResponse createUser(CreateUserRequest request){
        UserEntity userEntity = userMapper.toEntity(request);
        
        userEntity.setPassword(passwordEncoder.encode(request.password()));

        UserEntity userSaved = userRepository.save(userEntity);
        return userMapper.toResponse(userSaved);
    }
}

