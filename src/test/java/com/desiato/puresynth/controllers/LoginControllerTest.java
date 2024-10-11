package com.desiato.puresynth.controllers;

import com.desiato.puresynth.BaseTest;
import com.desiato.puresynth.dtos.AuthenticatedUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class LoginControllerTest extends BaseTest {

    private String uniqueEmail;
    private String validPassword;
    private String invalidPassword;
    private AuthenticatedUser authenticatedUser;


    @BeforeEach
    void setUp() throws Exception {

        authenticatedUser = testAuthenticationHelper.createAndAuthenticateUser();

        uniqueEmail = testAuthenticationHelper.generateUniqueEmail();
        validPassword = "password123";
        invalidPassword = "invalidPassword";

        userService.createUser(uniqueEmail, validPassword);
    }

    @Test
    void authenticateUser_WhenPasswordIsInvalid_ShouldReturnUnauthorized() throws Exception {

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
                .andExpect(jsonPath("$.message")
                        .value("Authentication failed. Please check your credentials and try again."));
    }

    @Test
    void authenticateUser_WhenCredentialsAreValid_ShouldAuthenticateSuccessfully() throws Exception {
        String loginJson = String.format("""
            {
                "email": "%s",
                "password": "%s"
            }
            """, uniqueEmail, validPassword);

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authToken").exists());
    }

    @Test
    void accessProtectedEndpoint_WithoutToken_ShouldDenyAccess() throws Exception {
        mockMvc.perform(get("/api/user/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void accessProtectedEndpoint_WithInvalidToken_ShouldDenyAccess() throws Exception {
        mockMvc.perform(get("/api/user/me")
                        .header("authToken", "invalid_token"))
                .andExpect(status().isUnauthorized());
    }
}
