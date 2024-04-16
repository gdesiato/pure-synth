package com.desiato.puresynth.models;

import com.desiato.puresynth.repositories.UserRepository;
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
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void createUser_ShouldReturnCreatedUser() throws Exception {
        String uniqueEmail = generateUniqueEmail();
        String newUserJson = """
    {
        "email": "%s",
        "password": "password123"
    }
    """.formatted(uniqueEmail);

        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUserJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(uniqueEmail));
    }

    private String generateUniqueEmail() {
        return "test_" + System.currentTimeMillis() + "@example.com";
    }

}
