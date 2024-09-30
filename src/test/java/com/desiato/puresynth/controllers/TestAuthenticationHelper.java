package com.desiato.puresynth.controllers;

import com.desiato.puresynth.dtos.AuthenticationRequestDTO;
import com.desiato.puresynth.dtos.PureSynthToken;
import com.desiato.puresynth.dtos.AuthenticatedUser;
import com.desiato.puresynth.models.Session;
import com.desiato.puresynth.models.User;
import com.desiato.puresynth.repositories.SessionRepository;
import com.desiato.puresynth.services.AuthenticationService;
import com.desiato.puresynth.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Service
public class TestAuthenticationHelper {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    public AuthenticatedUser createAndAuthenticateUser() throws Exception {
        String email = generateUniqueEmail();
        String password = "password123";

        User existingUser = userService.createUser(email, password);

        AuthenticationRequestDTO request = new AuthenticationRequestDTO(existingUser.getEmail(), password);

        PureSynthToken pureSynthToken = authenticationService.authenticate(request);

        Session userSession = new Session(pureSynthToken.value(), existingUser);

        return new AuthenticatedUser(existingUser, pureSynthToken);
    }

    public User createAndPersistUser() throws Exception {
        String email = generateUniqueEmail();
        String password = "password123";

        return userService.createUser(email, password);
    }

    public String generateUniqueEmail() {
        return "user_" + UUID.randomUUID().toString() + "@example.com";
    }
}
