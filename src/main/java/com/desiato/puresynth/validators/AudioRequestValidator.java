package com.desiato.puresynth.validators;

import com.desiato.puresynth.dtos.GenerateAudioRequestDTO;
import com.desiato.puresynth.exceptions.ErrorMessage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AudioRequestValidator extends AbstractValidator<GenerateAudioRequestDTO>{

    @Override
    protected void validate(GenerateAudioRequestDTO request, List<ErrorMessage> errorMessages) {

        // Validate frequency
        if (request.frequency() <= 0) {
            errorMessages.add(new ErrorMessage("Frequency must be greater than 0."));
        } else if (request.frequency() > 20000) {
            errorMessages.add(new ErrorMessage("Frequency must be less than or equal to 20,000 Hz."));
        }

        // Validate duration
        if (request.duration() <= 0) {
            errorMessages.add(new ErrorMessage("Duration must be greater than 0."));
        } else if (request.duration() > 600) {
            errorMessages.add(new ErrorMessage("Duration must be less than or equal to 600 seconds (10 minutes)."));
        }

    }
}
