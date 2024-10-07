package com.desiato.puresynth.validators;

import com.desiato.puresynth.dtos.AuthenticationRequestDTO;
import com.desiato.puresynth.exceptions.ErrorMessage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthenticationRequestValidator extends AbstractValidator<AuthenticationRequestDTO> {

    @Override
    protected void validate(AuthenticationRequestDTO request, List<ErrorMessage> errorMessages) {

        if (request.email() == null || request.email().isBlank()) {
            errorMessages.add(new ErrorMessage("Email cannot be blank."));
        } else if (!request.email().contains("@")) {
            errorMessages.add(new ErrorMessage("Invalid email format."));
        }

        if (request.password() == null || request.password().isBlank()) {
            errorMessages.add(new ErrorMessage("Password cannot be blank."));
        }
    }
}
