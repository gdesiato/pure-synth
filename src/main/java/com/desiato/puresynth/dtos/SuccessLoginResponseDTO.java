package com.desiato.puresynth.dtos;

public record SuccessLoginResponseDTO(String token, boolean success) implements LoginResponseDTO {

    @Override
    public String getMessage() {
        return "Success";
    }

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public String getToken() {
        return getToken();
    }
}
