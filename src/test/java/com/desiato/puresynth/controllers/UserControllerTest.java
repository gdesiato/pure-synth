package com.desiato.puresynth.controllers;

import com.desiato.puresynth.BaseTest;
import com.desiato.puresynth.dtos.AuthenticatedUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
public class UserControllerTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(LoginControllerTest.class);

    @Test
    public void getUser_ShouldReturnUser() throws Exception {
        AuthenticatedUser authenticatedUser = testAuthenticationHelper.createAndAuthenticateUser();

        mockMvc.perform(get("/api/user/" + authenticatedUser.user().getId())
                        .header("authToken", authenticatedUser.pureSynthToken().value()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(authenticatedUser.user().getId()))
                .andExpect(jsonPath("$.email").value(authenticatedUser.user().getEmail()));
    }

    @Test
    public void updateUser_ShouldUpdateUserAndReturnUpdatedUser() throws Exception {
        AuthenticatedUser authenticatedUser = testAuthenticationHelper.createAndAuthenticateUser();
        String newEmail = "newuser@example.com";
        String userJson = String.format("""
        {
            "email": "%s",
            "password": "password123"
        }
        """, newEmail);

        mockMvc.perform(put("/api/user/" + authenticatedUser.user().getId())
                        .header("authToken", authenticatedUser.pureSynthToken().value())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(newEmail));

        mockMvc.perform(get("/api/user/" + authenticatedUser.user().getId())
                        .header("authToken", authenticatedUser.pureSynthToken().value()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(newEmail))
                .andReturn();
    }

    @Test
    public void createUser_ShouldReturnCreatedUser() throws Exception {
        String uniqueEmail = testAuthenticationHelper.generateUniqueEmail();
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
                .andExpect(jsonPath("$.email").value(uniqueEmail));
    }

    @Test
    public void deleteUser_ShouldDeleteUserAndReturnOk() throws Exception {
        // Given
        AuthenticatedUser authenticatedUser = testAuthenticationHelper.createAndAuthenticateUser();
        Long userId = authenticatedUser.user().getId();

        // When
        mockMvc.perform(delete("/api/user/" + userId)
                        .header("authToken", authenticatedUser.pureSynthToken().value()))
                .andExpect(status().isOk());

        // Then
        mockMvc.perform(get("/api/user/" + userId)
                        .header("authToken", authenticatedUser.pureSynthToken().value()))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}
