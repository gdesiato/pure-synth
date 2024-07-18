package com.desiato.puresynth.controllers;

import com.desiato.puresynth.BaseTest;
import com.desiato.puresynth.dtos.AuthenticatedUser;
import com.desiato.puresynth.models.Session;
import com.desiato.puresynth.models.User;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
        AuthenticatedUser authenticatedUser = testAuthenticationHelper.createAndAuthenticateUser();
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
    public void getUserById_shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        AuthenticatedUser authenticatedUser = testAuthenticationHelper.createAndAuthenticateUser();
        Long nonExistentUserId = 9999L;

        mockMvc.perform(get("/api/user/" + nonExistentUserId)
                        .header("authToken", authenticatedUser.pureSynthToken().value()))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}
