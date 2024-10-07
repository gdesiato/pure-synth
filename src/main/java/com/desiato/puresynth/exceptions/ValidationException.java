package com.desiato.puresynth.exceptions;


import java.util.List;

public class ValidationException extends RuntimeException {

    private final List<ErrorMessage> errorMessages;

    public ValidationException(List<ErrorMessage> errorMessages) {
        super("Validation failed");
        this.errorMessages = errorMessages;
    }

    public List<ErrorMessage> getErrorMessages() {
        return errorMessages;
    }
}
