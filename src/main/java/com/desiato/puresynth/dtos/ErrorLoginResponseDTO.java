package com.desiato.puresynth.dtos;

public class ErrorLoginResponseDTO implements LoginResponseDTO {
    private final String message;
    private final boolean success = false;

    public ErrorLoginResponseDTO(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public String getToken() {
        return null;  // Typically, no token on error
    }
}
