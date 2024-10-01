package com.desiato.puresynth.validators;

import com.desiato.puresynth.dtos.UserRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserRequestValidator extends AbstractValidator<UserRequestDTO> {

    @Override
    public void validate(UserRequestDTO userRequestDTO) {

        log.info("email value is " + userRequestDTO.email());

        if (userRequestDTO.email() != null) {
            if (userRequestDTO.email().isBlank()) {
                addError("Email cannot be blank.");
            } else if (!userRequestDTO.email().contains("@")) {
                addError("Invalid email format.");
            }
        } else {
            addError("Email cannot be null.");
        }

        if (userRequestDTO.password() != null && !userRequestDTO.password().isBlank()) {
            log.info("Password is valid");
        } else if (userRequestDTO.password() == null) {
            addError("Password cannot be null.");
        } else {
            addError("Password cannot be blank.");
        }

        checkForErrors();
    }
}
