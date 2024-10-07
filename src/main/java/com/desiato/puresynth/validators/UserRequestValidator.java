package com.desiato.puresynth.validators;

import com.desiato.puresynth.dtos.UserRequestDTO;
import com.desiato.puresynth.exceptions.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class UserRequestValidator extends AbstractValidator<UserRequestDTO> {

    @Override
    protected void validate(UserRequestDTO userRequestDTO, List<ErrorMessage> errorMessages) {

        if (userRequestDTO.email() != null) {
            if (userRequestDTO.email().isBlank()) {
                errorMessages.add(new ErrorMessage("Email cannot be blank."));
            } else if (!userRequestDTO.email().contains("@")) {
                errorMessages.add(new ErrorMessage("Invalid email format."));
            }
        } else {
            errorMessages.add(new ErrorMessage("Email cannot be null."));
        }

        if (userRequestDTO.password() != null && !userRequestDTO.password().isBlank()) {
            log.info("Password is valid");
        } else if (userRequestDTO.password() == null) {
            errorMessages.add(new ErrorMessage("Password cannot be null."));
        } else {
            errorMessages.add(new ErrorMessage("Password cannot be blank."));
        }
    }
}
