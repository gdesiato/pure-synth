package com.desiato.puresynth.models;

import com.desiato.puresynth.configurations.SecurityConfig;
import com.desiato.puresynth.services.UserService;
import org.junit.jupiter.api.Test;
import com.desiato.puresynth.controllers.UserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void createUser_ShouldReturnCreatedUser() throws Exception {
        String newUserJson =
    """
    {
        "email": "test@example.com",
        "password": "password123"
    }
    """;

        User returnedUser = new User();
        returnedUser.setId(1L); // Simulating the ID assigned by the database or service layer
        returnedUser.setEmail("test@example.com");
        returnedUser.setPassword("password123");

        when(userService.findByEmail("test@example.com")).thenReturn(null);
        when(userService.saveUser(any(User.class))).thenReturn(returnedUser);

        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUserJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.id").value(1));
    }

}