package com.desiato.puresynth.controllers;

import com.desiato.puresynth.models.LoginDTO;
import com.desiato.puresynth.models.User;
import com.desiato.puresynth.repositories.UserRepository;
import com.desiato.puresynth.services.CustomAuthenticationService;
import com.desiato.puresynth.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomAuthenticationService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void authenticateUser_WhenCredentialsAreValid_ShouldAuthenticateSuccessfully() throws Exception {

        String uniqueEmail = generateUniqueEmail();
        String password = "password123";
        User createdUser = userService.createUser(uniqueEmail, passwordEncoder.encode(password));

        String json = "{\"email\":\"" + uniqueEmail + "\", \"password\":\"" + password + "\"}";

        mockMvc.perform(post("/api/login/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("User authenticated successfully"));
    }

    @Test
    public void authenticateUser_WhenPasswordIsInvalid_ShouldReturnUnauthorized() throws Exception {
        String uniqueEmail = generateUniqueEmail();
        String validPassword = "password123";
        userService.createUser(uniqueEmail, passwordEncoder.encode(validPassword));

        String invalidPassword = "invalidPassword";
        String json = "{\"email\":\"" + uniqueEmail + "\", \"password\":\"" + invalidPassword + "\"}";

        mockMvc.perform(post("/api/login/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Authentication failed"));
    }

    @Test
    public void authenticateUser_WhenEmailIsInvalid_ShouldReturnUnauthorized() throws Exception {
        String uniqueEmail = generateUniqueEmail();
        String password = "password123";
        userService.createUser(uniqueEmail, passwordEncoder.encode(password));

        String invalidEmail = "nonUser";
        String json = "{\"email\":\"" + invalidEmail + "\", \"password\":\"" + password + "\"}";

        mockMvc.perform(post("/api/login/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Authentication failed"));
    }

    private String generateUniqueEmail() {
        return "user_" + UUID.randomUUID().toString() + "@example.com";
    }
}
