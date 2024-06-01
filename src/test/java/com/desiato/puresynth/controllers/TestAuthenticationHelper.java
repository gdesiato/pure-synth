package com.desiato.puresynth.controllers;

import com.desiato.puresynth.dtos.AuthenticationRequestDTO;
import com.desiato.puresynth.dtos.PureSynthToken;
import com.desiato.puresynth.models.AuthenticatedUser;
import com.desiato.puresynth.models.User;
import com.desiato.puresynth.services.AuthenticationService;
import com.desiato.puresynth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TestAuthenticationHelper {
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final PasswordEncoder passwordEncoder;

    public TestAuthenticationHelper(UserService userService, AuthenticationService authenticationService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthenticatedUser createAndAuthenticateUser() throws Exception {
        String email = generateUniqueEmail();
        String password = "password123";
        String encodedPassword = passwordEncoder.encode(password);

        User existingUser = userService.createUser(email, encodedPassword);

        AuthenticationRequestDTO request = new AuthenticationRequestDTO(email, password);

        Optional<PureSynthToken> optionalToken = authenticationService.authenticate(request);
        PureSynthToken pureSynthToken = optionalToken.orElseThrow(() -> new RuntimeException("Authentication failed, no token obtained"));

        return new AuthenticatedUser(existingUser, pureSynthToken);
    }

    private String generateUniqueEmail() {
        return "user_" + UUID.randomUUID().toString() + "@example.com";
    }
}
