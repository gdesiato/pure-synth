package com.desiato.puresynth.controllers;

import com.desiato.puresynth.models.User;
import com.desiato.puresynth.services.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;




@SpringBootTest
@AutoConfigureMockMvc
public class AdminCRUDControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    // mocking authenticated user to access the protected endpoint /api/admin/users
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testGetAllUsers() throws Exception {

        // Create mock User objects
        User mockUser1 = new User();
        mockUser1.setUsername("user1");
        mockUser1.setEmail("user1@example.com");

        User mockUser2 = new User();
        mockUser2.setUsername("user2");
        mockUser2.setEmail("user2@example.com");

        // Mock the behavior of userService to return a list of users
        when(userService.getAllUsers()).thenReturn(Arrays.asList(mockUser1, mockUser2));

        // Perform the GET request and assert the response
        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))  // Expecting 2 users in the list
                .andExpect(jsonPath("$[0].username", is("user1")))
                .andExpect(jsonPath("$[0].email", is("user1@example.com")))
                .andExpect(jsonPath("$[1].username", is("user2")))
                .andExpect(jsonPath("$[1].email", is("user2@example.com")));
    }
}
