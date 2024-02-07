package com.desiato.puresynth.controllers;

import com.desiato.puresynth.configurations.SecurityConfig;
import com.desiato.puresynth.models.User;
import com.desiato.puresynth.repositories.RoleRepository;
import com.desiato.puresynth.repositories.UserRepository;
import com.desiato.puresynth.services.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AdminCRUDController.class)
@Import(SecurityConfig.class)
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

    private static final Logger logger = LoggerFactory.getLogger(AdminCRUDControllerTest.class);


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
        mockMvc.perform(get("/api/admin/"))
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

        mockMvc.perform(get("/api/admin/{id}", 1L))
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
        mockMvc.perform(get("/api/admin/{id}", 2L))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testCreateUser() throws Exception {
        User newUser = new User();
        newUser.setUsername("newUser");
        newUser.setEmail("newUser@example.com");

        // Simulate that the user does not exist already
        when(userService.findByUsername("newUser")).thenReturn(null);

        // Mock the behavior of userService to return newUser when saving
        when(userService.saveUser(any(User.class))).thenReturn(newUser);

        // Create a JSON string of the newUser object
        ObjectMapper objectMapper = new ObjectMapper();
        String newUserJson = objectMapper.writeValueAsString(newUser);

        // Perform the POST request and assert the response
        mockMvc.perform(post("/api/admin/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUserJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", is(newUser.getUsername())))
                .andExpect(jsonPath("$.email", is(newUser.getEmail())));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testUpdateUser_UserExists() throws Exception {
        Long userId = 1L;

        // Existing user
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUsername("existingUser");
        existingUser.setEmail("existing@example.com");

        // User details for update
        User updatedUserDetails = new User();
        updatedUserDetails.setUsername("updatedUser");
        updatedUserDetails.setEmail("updated@example.com");

        // Mock getUserById to return the existing user
        when(userService.getUserById(userId)).thenReturn(Optional.of(existingUser));

        // Mock saveUser to return updated user details
        when(userService.saveUser(any(User.class))).thenReturn(updatedUserDetails);

        // Convert updatedUserDetails to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(updatedUserDetails);

        // PUT request
        mockMvc.perform(put("/api/admin/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(updatedUserDetails.getUsername())))
                .andExpect(jsonPath("$.email", is(updatedUserDetails.getEmail())));

        // Verify interactions with userService
        verify(userService).getUserById(userId);
        verify(userService).saveUser(any(User.class));
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testUpdateUser_UserDoesNotExist() throws Exception {
        Long userId = 2L;

        // User details for update
        User updatedUserDetails = new User();
        updatedUserDetails.setUsername("updatedUser");
        updatedUserDetails.setEmail("updated@example.com");

        // Mock getUserById to return empty (user does not exist)
        when(userService.getUserById(userId)).thenReturn(Optional.empty());

        // Convert updatedUserDetails to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(updatedUserDetails);

        // PUT request
        mockMvc.perform(put("/api/admin/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isNotFound());

        // Verify interactions with userService
        verify(userService).getUserById(userId);
        verify(userService, never()).saveUser(any(User.class)); // Ensure saveUser is not called
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testDeleteUser_UserExists() throws Exception {
        Long userId = 1L;

        when(userService.getUserById(userId)).thenReturn(Optional.of(new User()));

        mockMvc.perform(delete("/api/admin/{id}", userId))
                .andExpect(status().isOk());

        // Verify interactions with userService
        verify(userService).getUserById(userId);
        verify(userService).deleteUser(userId);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testDeleteUser_UserDoesNotExist() throws Exception {
        Long userId = 2L;

        when(userService.getUserById(userId)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/admin/{id}", userId))
                .andExpect(status().isNotFound());

        verify(userService).getUserById(userId);
        verify(userService, never()).deleteUser(userId);
    }

}
