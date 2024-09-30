package com.desiato.puresynth.validators;

import com.desiato.puresynth.dtos.UserRequestDTO;
import com.desiato.puresynth.exceptions.ValidationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserRequestValidator {

    public void validateUserRequestDto(UserRequestDTO userRequestDTO) {
        List<String> errorMessages = new ArrayList<>();

        if (userRequestDTO.email() != null) {
            if (userRequestDTO.email().isBlank()) {
                errorMessages.add("Email cannot be blank");
            } else if (!userRequestDTO.email().contains("@")) {
                errorMessages.add("Invalid email format");
            }
        }

        if (userRequestDTO.password() != null) {
            if (userRequestDTO.password().isBlank()) {
                errorMessages.add("Password cannot be blank");
            }
        }

        if (!errorMessages.isEmpty()) {
            throw new ValidationException(errorMessages);
        }
    }
}
