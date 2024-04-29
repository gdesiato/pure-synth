package com.desiato.puresynth.controllers;

import com.desiato.puresynth.models.LoginDTO;
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
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDTO loginDto) {
        Optional<String> token = authService.authenticate(loginDto.email(), loginDto.password());

        return token.map(s -> ResponseEntity.ok()
                .header("Auth-Token", s)
                .body("User authenticated successfully"))
                .orElseGet(() ->
                        ResponseEntity.status(401).body("Authentication failed"));
    }
}
