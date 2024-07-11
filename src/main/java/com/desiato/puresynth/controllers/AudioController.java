package com.desiato.puresynth.controllers;

import com.desiato.puresynth.models.AudioRequest;
import com.desiato.puresynth.services.AudioService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.File;

@AllArgsConstructor
@RestController
@RequestMapping("/api/audio")
public class AudioController {

    private final AudioService audioService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateAudio(@RequestBody AudioRequest request) {
        try {
            byte[] audioBytes = audioService.generateSineWaveFile(request.getFrequency(), request.getDuration());
            ByteArrayResource resource = new ByteArrayResource(audioBytes);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"sine_wave_" + request.getFrequency() + "Hz.wav\"")
                    .contentType(MediaType.parseMediaType("audio/wav"))
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error generating audio: " + e.getMessage());
        }
    }
}
