package com.desiato.puresynth.controllers;

import com.desiato.puresynth.dtos.AuthenticationRequestDTO;
import com.desiato.puresynth.dtos.LoginResponseDTO;
import com.desiato.puresynth.dtos.LoginRequestDTO;
import com.desiato.puresynth.dtos.Token;
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
    public ResponseEntity<LoginResponseDTO> authenticateUser(@RequestBody AuthenticationRequestDTO requestDTO) {
        Optional<Token> optionalToken = authenticationService.authenticate(requestDTO);

        return optionalToken.map(token ->
                        ResponseEntity.ok(new LoginResponseDTO("Success", token.value(), true)))
                .orElseGet(() ->
                        ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(new LoginResponseDTO("Unauthorized", null, false)));
    }
}
