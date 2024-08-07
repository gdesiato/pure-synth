package com.desiato.puresynth.controllers;

import com.desiato.puresynth.BaseTest;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class LoginControllerTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(LoginControllerTest.class);

    @Test
    public void authenticateUser_WhenPasswordIsInvalid_ShouldReturnUnauthorized() throws Exception {
        String uniqueEmail = testAuthenticationHelper.generateUniqueEmail();
        String validPassword = "password123";
        userService.createUser(uniqueEmail, passwordEncoder.encode(validPassword));

        String invalidPassword = "invalidPassword";
        String json = """
                {
                    "email": "%s",
                    "password": "%s"
                }
                """.formatted(uniqueEmail, invalidPassword);

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Unauthorized"));
    }

    @Test
    public void authenticateUser_WhenCredentialsAreValid_ShouldAuthenticateSuccessfully() throws Exception {
        String uniqueEmail = testAuthenticationHelper.generateUniqueEmail();
        String password = "password123";

        userService.createUser(uniqueEmail, passwordEncoder.encode(password));
        logger.info("Created user with email: {}", uniqueEmail);

        String loginJson = String.format("""
            {
                "email": "%s",
                "password": "%s"
            }
            """, uniqueEmail, password);

        logger.info("Sending login request with email: {}", uniqueEmail);
        MvcResult loginResult = mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authToken").exists())
                .andReturn();

        logger.info("Login response status code: {}", loginResult.getResponse().getStatus());

        String token = JsonPath.parse(loginResult.getResponse().getContentAsString()).read("$.authToken", String.class);
        assertNotNull(token, "Authentication token is missing or invalid");
        logger.info("Retrieved token: {}", token);
    }

    @Test
    public void accessProtectedEndpoint_WithValidToken_ShouldAllowAccess() throws Exception {
        // Step 1: Create a valid user and authenticate
        String uniqueEmail = testAuthenticationHelper.generateUniqueEmail();
        String password = "password123";
        userService.createUser(uniqueEmail, passwordEncoder.encode(password));

        String loginJson = String.format("""
            {
                "email": "%s",
                "password": "%s"
            }
            """, uniqueEmail, password);

        MvcResult loginResult = mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authToken").exists())
                .andReturn();

        String token = JsonPath.parse(loginResult.getResponse().getContentAsString()).read("$.authToken", String.class);

        // Step 2: Access a protected endpoint using the valid token
        mockMvc.perform(get("/api/user/me")
                        .header("authToken", token))
                .andExpect(status().isOk());
    }

    @Test
    public void accessProtectedEndpoint_WithoutToken_ShouldDenyAccess() throws Exception {
        mockMvc.perform(get("/api/user/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void accessProtectedEndpoint_WithInvalidToken_ShouldDenyAccess() throws Exception {
        mockMvc.perform(get("/api/user/me")
                        .header("authToken", "invalid_token"))
                .andExpect(status().isUnauthorized());
    }
}
