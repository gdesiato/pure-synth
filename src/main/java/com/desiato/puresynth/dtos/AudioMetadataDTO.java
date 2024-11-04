package com.desiato.puresynth.dtos;

public record AudioMetadataDTO(
        Long id,
        String fileName,
        double frequency,
        double duration
) {
}
