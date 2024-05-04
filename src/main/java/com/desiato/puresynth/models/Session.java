package com.desiato.puresynth.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_session")
public class Session {
    @Id
    private String token;

    @Column(nullable = false)
    private String email;

    @ManyToOne
    @JoinColumn(name = "user_obj")
    private User user;

    private long loginTimestamp;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Session() {
    }

    public Session(String token, String email, User user, long loginTimestamp) {
        this.token = token;
        this.email = email;
        this.user = user;
        this.loginTimestamp = loginTimestamp;
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

    public long getLoginTimestamp() {
        return loginTimestamp;
    }

    public void setLoginTimestamp(long loginTimestamp) {
        this.loginTimestamp = loginTimestamp;
    }
}
