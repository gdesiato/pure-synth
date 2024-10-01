package com.desiato.puresynth.validators;

import com.desiato.puresynth.exceptions.ErrorMessage;
import com.desiato.puresynth.exceptions.ValidationException;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractValidator<T> {

    private final List<ErrorMessage> errorMessages = new ArrayList<>();

    protected void addError(String errorMessage) {
        errorMessages.add(new ErrorMessage(errorMessage));
    }

    protected void checkForErrors() {
        if (!errorMessages.isEmpty()) {
            throw new ValidationException(errorMessages);
        }
    }

    public abstract void validate(T request);
}
