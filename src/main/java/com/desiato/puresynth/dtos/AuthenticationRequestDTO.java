package com.desiato.puresynth.dtos;

public record AuthenticationRequestDTO(String email, String password) {

    @Override
    public String email() {
        return email;
    }

    @Override
    public String password() {
        return password;
    }
}
