package com.desiato.puresynth.controllers;

import com.desiato.puresynth.models.AudioRequest;
import com.desiato.puresynth.services.AudioService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${audio.files.directory}")
    private String audioFilesDir;

    private final AudioService audioService;


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

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getAudio(@PathVariable String filename) {
        try {
            Path file = Paths.get(audioFilesDir).resolve(filename).normalize();
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
