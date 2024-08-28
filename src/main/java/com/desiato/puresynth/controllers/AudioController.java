package com.desiato.puresynth.controllers;

import com.desiato.puresynth.models.AudioRequest;
import com.desiato.puresynth.services.AudioService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/audio")
public final class AudioController {

    private final AudioService audioService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateAudio(@RequestBody final AudioRequest request) {
        try {
            byte[] audioBytes = audioService.generateSineWaveFile(
                    request.getFrequency(),
                    request.getDuration());

            ByteArrayResource resource = new ByteArrayResource(audioBytes);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"sine_wave_" +
                                    request.getFrequency() + "Hz.wav\"")
                    .contentType(MediaType.parseMediaType("audio/wav"))
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    "Error generating audio: " +
                    e.getMessage());
        }
    }
}
