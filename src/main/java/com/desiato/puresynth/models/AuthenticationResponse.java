package com.desiato.puresynth.models;

public class AuthenticationResponse {
    private final String authToken;

    public AuthenticationResponse(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }
}
