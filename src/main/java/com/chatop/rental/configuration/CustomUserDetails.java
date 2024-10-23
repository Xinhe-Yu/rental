package com.chatop.rental.configuration;

import com.chatop.rental.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {
    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // Override getUsername to return email
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = user.getRole();
        return List.of(() -> role);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Default to true
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Default to true
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Default to true
    }

    @Override
    public boolean isEnabled() {
        return true; // Default to true
    }
}
