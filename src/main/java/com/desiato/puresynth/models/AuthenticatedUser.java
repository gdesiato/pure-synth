package com.desiato.puresynth.models;

public class AuthenticatedUser {
    private final User user;
    private final String authToken;

    public AuthenticatedUser(User user, String authToken) {
        this.user = user;
        this.authToken = authToken;
    }

    public User getUser() {
        return user;
    }

    public String getAuthToken() {
        return authToken;
    }
}
