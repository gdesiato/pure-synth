package com.desiato.puresynth.validators;

import com.desiato.puresynth.dtos.AuthenticationRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationRequestValidator extends AbstractValidator<AuthenticationRequestDTO> {

    @Override
    public void validate(AuthenticationRequestDTO request) {

        if (request.email() == null || request.email().isBlank()) {
            addError("Email cannot be blank.");
        } else if (!request.email().contains("@")) {
            addError("Invalid email format.");
        }

        if (request.password() == null || request.password().isBlank()) {
            addError("Password cannot be blank.");
        }

        checkForErrors();
    }
}
