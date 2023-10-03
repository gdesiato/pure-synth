package com.desiato.puresynth.controllers;

import com.desiato.puresynth.services.PdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/audio")
public class AudioController {

    @Autowired
    private PdService pdService;

    @PostMapping("/setFrequency")
    public ResponseEntity<String> setFrequency(@RequestParam float frequency) {
        pdService.setFrequency(frequency);
        return ResponseEntity.ok("Frequency set!");
    }
}
