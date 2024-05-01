package com.desiato.puresynth.controllers;

import com.desiato.puresynth.models.LoginRequestDTO;
import com.desiato.puresynth.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private AuthenticationService authService;

    @PostMapping("/")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequestDTO loginRequestDTO) {
        Optional<String> token = authService.authenticate(loginRequestDTO.email(), loginRequestDTO.password());

        if (token.isPresent()) {
            return ResponseEntity.ok()
                    .header("Auth-Token", token.get())
                    .build();
        } else {
            return ResponseEntity.status(401).body("Authentication failed");
        }
    }
}
