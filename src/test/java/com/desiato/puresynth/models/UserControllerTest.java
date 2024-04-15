package com.desiato.puresynth.models;

import com.desiato.puresynth.configurations.SecurityConfig;
import com.desiato.puresynth.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(com.desiato.puresynth.configurations.SecurityConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    @Transactional
    public void setup() {
        userRepository.deleteByEmail("test3@example.com");
    }

    @Test
    @Transactional
    public void createUser_ShouldReturnCreatedUser() throws Exception {
        String newUserJson = """
        {
            "email": "test3@example.com",
            "password": "password123"
        }
        """;

        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUserJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test3@example.com"));
    }

}