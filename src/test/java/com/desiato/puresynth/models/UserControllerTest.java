package com.desiato.puresynth.models;

import com.desiato.puresynth.repositories.UserRepository;
import com.desiato.puresynth.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


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
        String uniqueEmail = generateUniqueEmail();
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

    @Test
    public void getUser_ShouldReturnUSer()throws Exception {
        User existingUser = userService.createUser(generateUniqueEmail(), "password123" );

        // When & Then
        mockMvc.perform(get("/api/user/" + existingUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingUser.getId()))
                .andExpect(jsonPath("$.email").value(existingUser.getEmail()));
    }


    @Test
    public void updateUser_ShouldUpdateUserAndReturnUpdatedUser() throws Exception {
        // Given
        String originalEmail = "user@example.com";
        User createdUser = userService.createUser(originalEmail, "password123");
        Long userId = createdUser.getId();

        String newEmail = "newuser@example.com";
        String userJson = "{\"email\":\"" + newEmail + "\"}";

        // When
        mockMvc.perform(put("/api/user/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(newEmail));

        // Then
        mockMvc.perform(get("/api/user/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(newEmail))
                .andReturn();
    }

    private String generateUniqueEmail() {
        return "user_" + UUID.randomUUID().toString() + "@example.com";
    }

}
