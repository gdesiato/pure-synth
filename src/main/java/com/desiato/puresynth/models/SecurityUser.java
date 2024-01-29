package com.desiato.puresynth.models;

import jakarta.persistence.Entity;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class SecurityUser implements UserDetails {

    private static final Logger logger = LoggerFactory.getLogger(SecurityUser.class);
    private final User user;

    public SecurityUser(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Log the roles of the user
        logger.info("Loading authorities for user: {}", user.getUsername());
        Collection<? extends GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> {
                    String roleName = "ROLE_" + role.getName(); // Ensure the ROLE_ prefix is used
                    logger.info("Granting authority: {}", roleName);
                    return new SimpleGrantedAuthority(roleName);
                })
                .collect(Collectors.toSet());

        logger.info("Granted authorities for user {}: {}", user.getUsername(), authorities);
        return authorities;
    }

    public User getUser() {
        return this.user;
    }

    public Long getId() {
        return user.getId();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
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
        return true;
    }

}
