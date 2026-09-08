package com.simulador.financiero.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.simulador.financiero.entities.UserEntity;
import com.simulador.financiero.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration 
@RequiredArgsConstructor 
public class AppConfig {
    private final UserRepository userRepository;

    @Bean 
    public UserDetailsService userDetailsService(){
        return username -> {
            final UserEntity user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .build();
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    } 
}
