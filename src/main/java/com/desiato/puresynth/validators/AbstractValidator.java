package com.desiato.puresynth.validators;

import com.desiato.puresynth.exceptions.ErrorMessage;
import com.desiato.puresynth.exceptions.ValidationException;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractValidator<T> {

    protected abstract void validate(T request, List<ErrorMessage> errorMessages);

    public void validate(T request) {
        List<ErrorMessage> errorMessages = new ArrayList<>();
        validate(request, errorMessages);

        if (!errorMessages.isEmpty()) {
            throw new ValidationException(errorMessages);
        }
    }
}
