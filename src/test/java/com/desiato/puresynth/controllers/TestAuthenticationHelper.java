package com.desiato.puresynth.controllers;

import com.desiato.puresynth.dtos.AuthenticationRequestDTO;
import com.desiato.puresynth.dtos.Token;
import com.desiato.puresynth.models.AuthenticatedUser;
import com.desiato.puresynth.models.User;
import com.desiato.puresynth.services.AuthenticationService;
import com.desiato.puresynth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

public class TestAuthenticationHelper {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthenticatedUser createAndAuthenticateUser() throws Exception {
        String email = generateUniqueEmail();
        String password = "password123";
        String encodedPassword = passwordEncoder.encode(password);

        User existingUser = userService.createUser(email, encodedPassword);

        AuthenticationRequestDTO request = new AuthenticationRequestDTO(email, password);

        Optional<Token> optionalToken = authenticationService.authenticate(request);
        Token token = optionalToken.orElseThrow(() -> new RuntimeException("Authentication failed, no token obtained"));

        return new AuthenticatedUser(existingUser, token);
    }

    private String generateUniqueEmail() {
        return "user_" + UUID.randomUUID().toString() + "@example.com";
    }
}
