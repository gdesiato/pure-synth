package com.desiato.puresynth.controllers;

import com.desiato.puresynth.BaseTest;
import com.desiato.puresynth.models.User;
import com.desiato.puresynth.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class LoginControllerTest extends BaseTest {

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Test
    public void authenticateUser_WhenCredentialsAreValid_ShouldAuthenticateSuccessfully() throws Exception {
        String uniqueEmail = generateUniqueEmail();
        String password = "password123";
        userService.createUser(uniqueEmail, passwordEncoder.encode(password));

        String json = """
                {
                    "email": "%s",
                    "password": "%s"
                }
                """.formatted(uniqueEmail, password);

        MvcResult loginResult = mockMvc.perform(post("/api/login/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(header().exists("Auth-Token"))
                .andReturn();

        String token = loginResult.getResponse().getHeader("Auth-Token");

        // Use the token to access a protected endpoint
        mockMvc.perform(get("/api/protected")
                        .header("Auth-Token", token))
                .andExpect(status().isOk());

        // Cleanup
        sessionRepository.deleteById(token);
        userRepository.deleteByEmail(uniqueEmail);
    }

    @Test
    public void authenticateUser_WhenPasswordIsInvalid_ShouldReturnUnauthorized() throws Exception {
        String uniqueEmail = generateUniqueEmail();
        String validPassword = "password123";
        userService.createUser(uniqueEmail, passwordEncoder.encode(validPassword));

        String invalidPassword = "invalidPassword";
        String json = """
                {
                    "email": "%s",
                    "password": "%s"
                }
                """.formatted(uniqueEmail, invalidPassword);

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

        String invalidEmail = "nonUser@mail.com";
        String json = """
                {
                    "email": "%s",
                    "password": "%s"
                }
                """.formatted(invalidEmail, password);

        mockMvc.perform(post("/api/login/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Authentication failed"));
    }

    @Test
    public void accessProtectedEndpoint_WithValidToken_ShouldAllowAccess() throws Exception {
        // Step 1: Create a valid user and authenticate
        String uniqueEmail = generateUniqueEmail();
        String password = "password123";
        userService.createUser(uniqueEmail, passwordEncoder.encode(password));

        String loginJson = """
                {
                    "email": "%s",
                    "password": "%s"
                }
                """.formatted(uniqueEmail, password);

        MvcResult loginResult = mockMvc.perform(post("/api/login/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(header().exists("Auth-Token"))
                .andReturn();

        String token = loginResult.getResponse().getHeader("Auth-Token");

        // Step 2: Access a protected endpoint using the valid token
        mockMvc.perform(get("/api/protected")
                        .header("Auth-Token", token))
                .andExpect(status().isOk());  // Access is granted
    }

    @Test
    public void accessProtectedEndpoint_WithoutToken_ShouldDenyAccess() throws Exception {
        mockMvc.perform(get("/api/protected"))
                .andExpect(status().isUnauthorized());  // No token, so access is denied
    }

    @Test
    public void accessProtectedEndpoint_WithInvalidToken_ShouldDenyAccess() throws Exception {
        mockMvc.perform(get("/api/protected")
                        .header("Auth-Token", "invalid_token"))
                .andExpect(status().isUnauthorized());  // Invalid token, so access is denied
    }

    private String generateUniqueEmail() {
        return "user_" + UUID.randomUUID().toString() + "@example.com";
    }
}