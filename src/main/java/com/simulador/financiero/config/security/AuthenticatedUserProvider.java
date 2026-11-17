package com.simulador.financiero.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.simulador.financiero.exceptions.BadRequestException;

public class AuthenticatedUserProvider {

    private AuthenticatedUserProvider() {
    }

    public static AuthenticatedUser getAuthenticatedUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof CustomUserDetails user)) {
            throw new BadRequestException(
                    "El usuario autenticado no es un CustomUserDetails"
            );
        }

        return new AuthenticatedUser(
                user.getUserId(),
                user.getEmail(),
                user.getUsername()
        );
    }
}