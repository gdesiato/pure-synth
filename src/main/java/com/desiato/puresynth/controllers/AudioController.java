package com.desiato.puresynth.controllers;

import com.desiato.puresynth.models.AudioRequest;
import com.desiato.puresynth.services.AudioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequestMapping("/audio")
public class AudioController {

    @Autowired
    private AudioService audioService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateAudio(@RequestBody AudioRequest request) {
        try {
            File audioFile = audioService.generateSineWaveFile(request.getFrequency(), request.getDuration());
            String downloadUrl = "http://localhost:8080/audio/" + audioFile.getName();
            return ResponseEntity.ok().body(downloadUrl);
        } catch (Exception e) {
            // Exception handling
            return ResponseEntity.internalServerError().body("Error generating audio: " + e.getMessage());
        }
    }
}
