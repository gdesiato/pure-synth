package com.desiato.puresynth.controllers;

import com.desiato.puresynth.BaseTest;
import com.desiato.puresynth.models.AuthenticatedUser;
import com.desiato.puresynth.models.CustomUserDetails;
import com.desiato.puresynth.models.User;
import com.desiato.puresynth.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest extends BaseTest {

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(LoginControllerTest.class);

    @Test
    public void getUser_ShouldReturnUser() throws Exception {

        AuthenticatedUser authenticatedUser = createAndAuthenticateUser();
        User existingUser = authenticatedUser.getUser();
        String token = authenticatedUser.getAuthToken();

        mockMvc.perform(get("/api/user/" + existingUser.getId())
                        .header("Auth-Token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingUser.getId()))
                .andExpect(jsonPath("$.email").value(existingUser.getEmail()));
    }

    @Test
    public void updateUser_ShouldUpdateUserAndReturnUpdatedUser() throws Exception {

        AuthenticatedUser authenticatedUser = createAndAuthenticateUser();
        User existingUser = authenticatedUser.getUser();
        String token = authenticatedUser.getAuthToken();
        Long userId = existingUser.getId();

        String newEmail = "newuser@example.com";
        String userJson = String.format("""
    {
        "email": "%s",
        "password": "password123"
    }
    """, newEmail);

        mockMvc.perform(put("/api/user/" + userId)
                        .header("Auth-Token", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(newEmail));

        mockMvc.perform(get("/api/user/" + userId)
                        .header("Auth-Token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(newEmail))
                .andReturn();
    }

    private String generateUniqueEmail() {
        return "user_" + UUID.randomUUID().toString() + "@example.com";
    }

    private AuthenticatedUser createAndAuthenticateUser() throws Exception {
        String email = generateUniqueEmail();
        String password = "password123";
        String encodedPassword = passwordEncoder.encode(password);

        User existingUser = userService.createUser(email, encodedPassword);

        Optional<String> optionalToken = authService.authenticate(email, password);
        assertTrue(optionalToken.isPresent(), "Authentication failed, no token obtained.");
        String token = optionalToken.get();

        return new AuthenticatedUser(existingUser, token);
    }

}
