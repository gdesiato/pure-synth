package com.desiato.puresynth.exceptions;

import java.util.List;

public class ValidationException extends RuntimeException{

    private final List<String> errorMessages;

    public ValidationException(List<String> errorMessages) {
        super("Validation failed");
        this.errorMessages = errorMessages;
    }
}
