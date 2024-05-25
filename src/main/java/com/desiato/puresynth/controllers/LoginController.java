package com.desiato.puresynth.controllers;

import com.desiato.puresynth.models.ApiResponse;
import com.desiato.puresynth.models.LoginRequestDTO;
import com.desiato.puresynth.services.AuthenticationService;
import com.desiato.puresynth.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    private UserService userService;
    private AuthenticationService authenticationService;

    public LoginController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }


    @PostMapping
    public ResponseEntity<ApiResponse> authenticateUser(@RequestBody LoginRequestDTO loginRequestDTO) {
        Optional<String> token = authenticationService.authenticate(loginRequestDTO.email(), loginRequestDTO.password());

        if (token.isPresent()) {
            ApiResponse response = new ApiResponse("Success", token.get(), true);
            return ResponseEntity.ok(response);
        } else {
            ApiResponse response = new ApiResponse("Unauthorized", null, false);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
