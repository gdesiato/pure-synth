package com.desiato.puresynth.controllers;

import com.desiato.puresynth.BaseTest;
import com.desiato.puresynth.models.User;
import com.desiato.puresynth.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest extends BaseTest {

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(LoginControllerTest.class);

    @Test
    public void createUser_ShouldReturnCreatedUser() throws Exception {
        String uniqueEmail = generateUniqueEmail();
        String newUserJson = String.format("""
        {
            "email": "%s",
            "password": "password123"
        }
        """, uniqueEmail);

        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUserJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(uniqueEmail))
                .andDo(print());
    }

    @Test
    public void deleteUser_ShouldDeleteUserAndReturnOk() throws Exception {
        // Given
        String email = generateUniqueEmail();
        String password = "password123";
        User existingUser = userService.createUser(email, passwordEncoder.encode(password));
        Long userId = existingUser.getId();

        Optional<String> optionalToken = authService.authenticate(email, password);
        assertTrue(optionalToken.isPresent(), "Authentication failed, no token obtained.");
        String token = optionalToken.get();

        userService.deleteUserSessions(userId);

        // When
        mockMvc.perform(delete("/api/user/" + userId)
                        .header("Auth-Token", token))
                .andExpect(status().isOk());

        // Then
        // Check that the user is deleted
        mockMvc.perform(get("/api/user/" + userId))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void getUser_ShouldReturnUser() throws Exception {
        User existingUser = userService.createUser(generateUniqueEmail(),
                passwordEncoder.encode("password123"));

        Optional<String> optionalToken = authService.authenticate(existingUser.getEmail(), "password123");
        assertTrue(optionalToken.isPresent());
        String token = optionalToken.get();

        mockMvc.perform(get("/api/user/" + existingUser.getId())
                        .header("Auth-Token", token))  // Add token to request header
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingUser.getId()))
                .andExpect(jsonPath("$.email").value(existingUser.getEmail()));
    }

    @Test
    public void updateUser_ShouldUpdateUserAndReturnUpdatedUser() throws Exception {
        // Given
        User existingUser = userService.createUser(generateUniqueEmail(),
                passwordEncoder.encode("password123"));
        Long userId = existingUser.getId();

        // Authenticate to obtain token
        Optional<String> optionalToken = authService.authenticate(existingUser.getEmail(), "password123");
        assertTrue(optionalToken.isPresent());
        String token = optionalToken.get();

        String newEmail = "newuser@example.com";
        String userJson = String.format("""
        {
            "email": "%s",
            "password": "password123"
        }
        """, newEmail);

        // When
        mockMvc.perform(put("/api/user/" + userId)
                        .header("Auth-Token", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(newEmail));

        // Then
        mockMvc.perform(get("/api/user/" + userId)
                        .header("Auth-Token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(newEmail))
                .andReturn();
    }

    @Test
    public void helloApi_ReturnsOk_WhenTokenIsValid() throws Exception {
        User existingUser = userService.createUser(generateUniqueEmail(), passwordEncoder.encode("password123"));
        Optional<String> optionalToken = authService.authenticate(existingUser.getEmail(), "password123");
        assertTrue(optionalToken.isPresent());
        String token = optionalToken.get();

        mockMvc.perform(get("/api/user/hello")
                        .header("Auth-Token", token))
                .andExpect(status().isOk())
                .andExpect(content().string("Access to protected resource granted"));
    }

    @Test
    public void helloApi_ReturnsUnauthorized_WhenTokenIsInvalid() throws Exception {
        String invalidToken = "this_is_an_invalid_token";

        mockMvc.perform(get("/api/user/hello")
                        .header("Auth-Token", invalidToken))
                .andExpect(status().isUnauthorized());
    }

    private String generateUniqueEmail() {
        return "user_" + UUID.randomUUID().toString() + "@example.com";
    }

}
