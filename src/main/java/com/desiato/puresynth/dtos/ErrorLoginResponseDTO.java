package com.desiato.puresynth.dtos;

public record ErrorLoginResponseDTO(String message, boolean success) implements LoginResponseDTO {
    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getToken() {
        return null;
    }

    @Override
    public boolean isSuccess() {
        return false;
    }
}
