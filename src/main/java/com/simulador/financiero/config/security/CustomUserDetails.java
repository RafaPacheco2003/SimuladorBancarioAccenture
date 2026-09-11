package com.simulador.financiero.config.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.simulador.financiero.entities.UserEntity;

import lombok.Getter;

@Getter
public class CustomUserDetails implements UserDetails {

    private final Long userId;
    private final String username;
    private final String password;
    private final String email; 
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean isEnabled;

    public CustomUserDetails(UserEntity user, Collection<? extends GrantedAuthority> authorities) {
        this.userId = user.getId();
        this.username = user.getFullName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.authorities = authorities;
        this.isEnabled = true;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
