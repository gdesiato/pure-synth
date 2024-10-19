package com.desiato.puresynth.controllers;

import com.desiato.puresynth.models.AudioRequest;
import com.desiato.puresynth.services.AudioService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/audio")
public final class AudioController {

    private final AudioService audioService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateAudio(@RequestBody final AudioRequest request) {

        try {
            // Generate the audio file as a byte array
            byte[] audioBytes = audioService.generateSineWaveFile(
                    request.getFrequency(),
                    request.getDuration());

            String fileName = "sine_wave_" + request.getFrequency() + "Hz";

            String filePath = audioService.saveAudioFile(audioBytes, fileName);

            return ResponseEntity.ok("File saved at: " + filePath);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    "Error generating audio: " + e.getMessage());
        }
    }
}
