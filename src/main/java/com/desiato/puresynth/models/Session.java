package com.desiato.puresynth.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_session")
public class Session {
    @Id
    private String token;

    @Column(nullable = false)
    private String email;

    @Column(nullable = true)
    private LocalDateTime expiration;

    public Session() {}

    public Session(String token, String email, LocalDateTime expiration) {
        this.token = token;
        this.email = email;
        this.expiration = expiration;
    }

    public LocalDateTime getExpiration() {
        return expiration;
    }

    public void setExpiration(LocalDateTime expiration) {
        this.expiration = expiration;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
