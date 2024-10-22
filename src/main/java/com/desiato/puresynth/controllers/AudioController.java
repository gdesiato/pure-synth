package com.desiato.puresynth.controllers;

import com.desiato.puresynth.models.AudioFile;
import com.desiato.puresynth.models.AudioRequest;
import com.desiato.puresynth.services.AudioService;
import com.desiato.puresynth.services.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ByteArrayResource;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/audio")
public final class AudioController {

    private final AudioService audioService;
    private final FileStorageService fileStorageService;

    @PostMapping
    public ResponseEntity<?> generateAudio(@RequestBody final AudioRequest request) {
        try {
            AudioFile audioFile = audioService.generateAndSaveSineWaveFile(
                    request.getFrequency(),
                    request.getDuration());

            return ResponseEntity.ok("File generated: " + audioFile.fileName());

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error generating audio: " + e.getMessage());
        }
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> retrieveAudio(@PathVariable String fileName) {
        try {
            AudioFile audioFile = fileStorageService.retrieveFile(fileName);

            Resource fileResource = new ByteArrayResource(audioFile.data());

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(fileResource);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}
