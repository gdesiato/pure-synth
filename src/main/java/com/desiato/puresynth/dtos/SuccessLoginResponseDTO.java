package com.desiato.puresynth.dtos;

public class SuccessLoginResponseDTO implements LoginResponseDTO {
    private final String token;
    private final boolean success = true;

    public SuccessLoginResponseDTO(String token) {
        this.token = token;
    }

    @Override
    public String getMessage() {
        return "Success";
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public String getToken() {
        return token;
    }
}
