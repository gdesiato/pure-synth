package com.desiato.puresynth.controllers;

import com.desiato.puresynth.models.LoginRequestDTO;
import com.desiato.puresynth.models.User;
import com.desiato.puresynth.services.AuthenticationService;
import com.desiato.puresynth.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService){
        this.userService = userService;
    }

    @Autowired
    private AuthenticationService authService;

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @PostMapping
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequestDTO loginRequestDTO) {
        logger.debug("~~~~~~~~=========authenticateUser() called========~~~~~~~~~");
        Optional<String> token = authService.authenticate(loginRequestDTO.email(), loginRequestDTO.password());

        if (token.isPresent()) {
            Map<String, String> tokenResponse = Map.of("Auth-Token", token.get());
            return ResponseEntity.ok(tokenResponse); //token inside response body
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
    }
}
