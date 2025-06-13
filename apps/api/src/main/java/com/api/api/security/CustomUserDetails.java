package com.api.api.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.UUID;

// Extiende la clase User de Spring Security para añadir el ID del usuario
public class CustomUserDetails extends User {

    private final UUID id; // ID del usuario

    public CustomUserDetails(UUID id, String username, String password,
            Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
