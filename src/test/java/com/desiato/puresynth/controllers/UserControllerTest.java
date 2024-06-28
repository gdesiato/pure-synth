package com.desiato.puresynth.controllers;

import com.desiato.puresynth.BaseTest;
import com.desiato.puresynth.models.AuthenticatedUser;
import com.desiato.puresynth.models.User;
import com.desiato.puresynth.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(LoginControllerTest.class);

    @Test
    public void getUser_ShouldReturnUser() throws Exception {
        AuthenticatedUser authenticatedUser = testAuthenticationHelper.createAndAuthenticateUser();

        mockMvc.perform(get("/api/user/" + authenticatedUser.user().getId())
                        .header("authToken", authenticatedUser.pureSynthToken()))
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
                        .header("authToken", authenticatedUser.pureSynthToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(newEmail));

        mockMvc.perform(get("/api/user/" + authenticatedUser.user().getId())
                        .header("authToken", authenticatedUser.pureSynthToken()))
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
                .andExpect(jsonPath("$.email").value(uniqueEmail))
                .andDo(print());
    }

    @Test
    public void deleteUser_ShouldDeleteUserAndReturnOk() throws Exception {
        // Given
        String uniqueEmail = testAuthenticationHelper.generateUniqueEmail();
        User createdUser = userService.createUser(uniqueEmail, "password123");
        Long userId = createdUser.getId();

        // When
        mockMvc.perform(delete("/api/user/" + userId))
                .andExpect(status().isOk());

        // Then
        mockMvc.perform(get("/api/user/" + userId))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}
