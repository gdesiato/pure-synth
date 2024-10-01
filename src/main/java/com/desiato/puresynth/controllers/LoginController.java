package com.desiato.puresynth.controllers;

import com.desiato.puresynth.dtos.*;
import com.desiato.puresynth.services.AuthenticationService;
import com.desiato.puresynth.services.UserService;
import com.desiato.puresynth.validators.AuthenticationRequestValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final AuthenticationService authenticationService;
    private final AuthenticationRequestValidator validator;

    @PostMapping
    public ResponseEntity<LoginResponseDTO> authenticateUser(
            @RequestBody AuthenticationRequestDTO requestDTO) {

        validator.validate(requestDTO);

        PureSynthToken pureSynthToken = authenticationService.authenticate(requestDTO);
        return ResponseEntity.ok(new LoginResponseDTO(pureSynthToken.value(), "success"));
    }
}

