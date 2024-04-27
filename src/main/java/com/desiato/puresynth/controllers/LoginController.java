package com.desiato.puresynth.controllers;

import com.desiato.puresynth.models.LoginDTO;
import com.desiato.puresynth.services.CustomAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private CustomAuthenticationService authService;

    @PostMapping("/")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDTO loginDto) {
        boolean isAuthenticated = authService.authenticate(loginDto.getEmail(), loginDto.getPassword());

        if (isAuthenticated) {
            return ResponseEntity.ok("User authenticated successfully");
        } else {
            return ResponseEntity.status(401).body("Authentication failed");
        }
    }
}
