package com.desiato.puresynth.controllers;

import com.desiato.puresynth.models.User;
import com.desiato.puresynth.repositories.RoleRepository;
import com.desiato.puresynth.repositories.UserRepository;
import com.desiato.puresynth.restControllers.AdminCRUDController;
import com.desiato.puresynth.services.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AdminCRUDController.class)
public class AdminCRUDControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;


    @Test
    // mocking authenticated user to access the protected endpoint /api/admin/users
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testGetAllUsers() throws Exception {

        // Create mock User
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

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testGetUserById_UserExists() throws Exception {

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("user1");
        mockUser.setEmail("user1@example.com");

        when(userService.getUserById(1L)).thenReturn(Optional.of(mockUser));

        mockMvc.perform(get("/api/admin/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("user1")))
                .andExpect(jsonPath("$.email", is("user1@example.com")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testGetUserById_UserDoesNotExist() throws Exception {
        // Mock the behavior of userService for non-existing user
        when(userService.getUserById(2L)).thenReturn(Optional.empty());

        // for non-existing user
        mockMvc.perform(get("/api/admin/users/{id}", 2L))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testCreateUser() throws Exception {
        // mock user
        User newUser = new User();
        newUser.setUsername("newUser");
        newUser.setEmail("newUser@example.com");

        // mock user to be returned by userService
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername(newUser.getUsername());
        savedUser.setEmail(newUser.getEmail());

        // Mock the behavior of userService to return the saved user
        when(userService.saveUser(any(User.class))).thenReturn(savedUser);

        // Convert newUser object to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String newUserJson = objectMapper.writeValueAsString(newUser);

        // Perform the POST request, and assert the response
        mockMvc.perform(post("/api/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUserJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("newUser")))
                .andExpect(jsonPath("$.email", is("newUser@example.com")));

        // Verify that userService.saveUser was called with any User object
        verify(userService).saveUser(any(User.class));
    }
}
