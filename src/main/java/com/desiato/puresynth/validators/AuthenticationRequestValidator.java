package com.desiato.puresynth.validators;

import com.desiato.puresynth.dtos.AuthenticationRequestDTO;
import com.desiato.puresynth.exceptions.ValidationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthenticationRequestValidator {

    public void validateAuthenticationRequestDTO(AuthenticationRequestDTO request) {

        List<String> errorMessages = new ArrayList<>();

        if (request.email() == null || request.email().isBlank()) {
            errorMessages.add("Email cannot be blank.");
        } else if (!request.email().contains("@")) {
            errorMessages.add("Invalid email format.");
        }

        if (request.password() == null || request.password().isBlank()) {
            errorMessages.add("Password cannot be blank.");
        }

        if (!errorMessages.isEmpty()) {
            throw new ValidationException(errorMessages);
        }
    }
}
