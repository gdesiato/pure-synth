package com.desiato.puresynth.controllers;

import com.desiato.puresynth.BaseTest;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class LoginControllerTest extends BaseTest {

    @Test
    void authenticateUser_WhenPasswordIsInvalid_ShouldReturnUnauthorized() throws Exception {
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
                .andExpect(jsonPath("$.message").value("Authentication failed. Please check your credentials and try again."));
    }

    @Test
    void authenticateUser_WhenCredentialsAreValid_ShouldAuthenticateSuccessfully() throws Exception {
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
        assertNotNull(token, "Authentication token is missing or invalid");
    }

    @Test
    void accessProtectedEndpoint_WithValidToken_ShouldAllowAccess() throws Exception {
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
