package com.desiato.puresynth.controllers;

import com.desiato.puresynth.BaseTest;
import com.desiato.puresynth.dtos.AuthenticatedUser;
import com.desiato.puresynth.models.Session;
import com.desiato.puresynth.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest extends BaseTest {

    private AuthenticatedUser authenticatedUser;
    private String validEmail;

    @BeforeEach
    void setUp() throws Exception {
        authenticatedUser = testAuthenticationHelper.createAndAuthenticateUser();
        validEmail = testAuthenticationHelper.generateUniqueEmail();
    }

    @Test
    void getUser_ShouldReturnUser() throws Exception {

        mockMvc.perform(get("/api/user/" + authenticatedUser.user().getId())
                        .header("authToken", authenticatedUser.pureSynthToken().value()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(authenticatedUser.user().getId()))
                .andExpect(jsonPath("$.email").value(authenticatedUser.user().getEmail()));
    }

    @Test
    void updateUser_ShouldUpdateUserAndReturnUpdatedUser() throws Exception {
        String userJson = String.format("""
        {
            "email": "%s",
            "password": "password123"
        }
        """, validEmail);

        mockMvc.perform(put("/api/user/" + authenticatedUser.user().getId())
                        .header("authToken", authenticatedUser.pureSynthToken().value())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(validEmail));

        mockMvc.perform(get("/api/user/" + authenticatedUser.user().getId())
                        .header("authToken", authenticatedUser.pureSynthToken().value()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(validEmail))
                .andReturn();
    }

    @Test
    void createUser_ShouldReturnCreatedUser() throws Exception {
        String newUserJson = String.format("""
    {
        "email": "%s",
        "password": "password123"
    }
    """, validEmail);

        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUserJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(validEmail));
    }

    @Test
    void deleteUser_ShouldDeleteUserAndReturnOk() throws Exception {
        Long userId = authenticatedUser.user().getId();
        String token = authenticatedUser.pureSynthToken().value();

        mockMvc.perform(delete("/api/user/" + userId)
                        .header("authToken", token))
                .andExpect(status().isNoContent());

        Optional<User> deletedUser = userRepository.findById(userId);
        Optional<Session> deletedSession = sessionRepository.findByToken(token);

        assertThat(deletedSession.isPresent()).isFalse();
        assertThat(deletedUser.isPresent()).isFalse();
    }

    @Test
    void getUserById_shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        Long nonExistentUserId = 9999L;

        mockMvc.perform(get("/api/user/" + nonExistentUserId)
                        .header("authToken", authenticatedUser.pureSynthToken().value()))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}
