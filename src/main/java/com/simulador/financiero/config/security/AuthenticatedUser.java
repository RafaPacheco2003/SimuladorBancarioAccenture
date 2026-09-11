package com.simulador.financiero.config.security;

public record AuthenticatedUser(Long id, String email, String fullName) {
    
}
