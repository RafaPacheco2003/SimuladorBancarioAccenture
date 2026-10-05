package com.simulador.financiero.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.simulador.financiero.Exceptions.BadRequestException;
import com.simulador.financiero.config.security.AuthenticatedUser;
import com.simulador.financiero.config.security.CustomUserDetails;

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