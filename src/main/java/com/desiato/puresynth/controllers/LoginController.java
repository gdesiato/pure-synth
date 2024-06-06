package com.desiato.puresynth.controllers;

import com.desiato.puresynth.dtos.*;
import com.desiato.puresynth.services.AuthenticationService;
import com.desiato.puresynth.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    public LoginController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @PostMapping
    public ResponseEntity<LoginResponseDTO> authenticateUser(@RequestBody AuthenticationRequestDTO requestDTO) {
        PureSynthToken pureSynthToken = authenticationService.authenticate(requestDTO);
        return ResponseEntity.ok(new LoginResponseDTO(pureSynthToken.value(), "success"));
    }
}
