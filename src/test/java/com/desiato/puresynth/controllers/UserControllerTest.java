package com.desiato.puresynth.controllers;

import com.desiato.puresynth.configurations.SecurityConfig;
import com.desiato.puresynth.models.Role;
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

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
public class UserControllerTest {

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

    private static final Logger logger = LoggerFactory.getLogger(UserControllerTest.class);



    @Test
    @WithMockUser(username = "testuser")
    public void testHelloApi() throws Exception {
        mockMvc.perform(get("/api/user"))
                .andExpect(status().isOk())
                .andExpect(content().string("hello api"));
    }

    @Test
    @WithMockUser(username = "testuser")
    public void testUserInfo() throws Exception {
        mockMvc.perform(get("/api/user/info"))
                .andExpect(status().isOk())
                .andExpect(content().string("Access granted for user: testuser"));
    }

    @Test
    public void testRegisterUser_Basic() throws Exception {
        logger.info("Starting testRegisterUser");

        User newUser = new User();
        newUser.setUsername("newUser");
        newUser.setEmail("newUser@test.com");
        newUser.setPassword("newPassword");

        Role userRole = new Role();
        userRole.setName("USER");
        newUser.setRoles(Collections.singleton(userRole));

        // Mocking userService behavior
        when(userService.findByUsername("newUser")).thenReturn(null);
        when(userService.saveUser(any(User.class))).thenReturn(newUser);

        ObjectMapper objectMapper = new ObjectMapper();
        String newUserJson = objectMapper.writeValueAsString(newUser);

        logger.info("Performing POST request to register a new user");
        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUserJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("newUser"))
                .andExpect(jsonPath("$.email").value("newUser@test.com"));

        logger.info("Verifying that userService methods are called as expected");
        verify(userService).findByUsername("newUser");
        verify(userService).saveUser(any(User.class));

        logger.info("TestRegisterUser completed");
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    public void testUpdateMyProfile_UserExists() throws Exception {

        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUsername("testUser");
        existingUser.setEmail("testuser@test.com");

        User updatedUserDetails = new User();
        updatedUserDetails.setUsername("testUserUpdated");
        updatedUserDetails.setEmail("updated@test.com");

        // Assume userService.findByUsername will return the existingUser for authorization check
        when(userService.getUserById(userId)).thenReturn(Optional.of(existingUser));

        // Mock the behavior of userService.saveUser to return the updated user details
        when(userService.saveUser(any(User.class))).thenReturn(updatedUserDetails);

        ObjectMapper objectMapper = new ObjectMapper();
        String updatedUserJson = objectMapper.writeValueAsString(updatedUserDetails);

        mockMvc.perform(put("/api/user/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedUserJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(updatedUserDetails.getUsername()))
                .andExpect(jsonPath("$.email").value(updatedUserDetails.getEmail()));

        // Verify userService was called with the correct parameters
        verify(userService).getUserById(userId);
        verify(userService).saveUser(any(User.class));

        //verify(userService, times(2)).saveUser(any(User.class));

    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    public void testUpdateMyProfile_UserDoesNotExist() throws Exception {

        Long userId = 1L;
        User updatedUserDetails = new User();
        updatedUserDetails.setUsername("testUser");
        updatedUserDetails.setEmail("updated@test.com");

        when(userService.getUserById(userId)).thenReturn(Optional.empty());

        ObjectMapper objectMapper = new ObjectMapper();
        String updatedUserJson = objectMapper.writeValueAsString(updatedUserDetails);

        mockMvc.perform(put("/api/user/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedUserJson))
                .andExpect(status().isNotFound());

        verify(userService).getUserById(userId);
    }


    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    public void deleteUser_WhenUserExists() throws Exception {

        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(userService.getUserById(userId)).thenReturn(Optional.of(user));

        mockMvc.perform(delete("/api/user/{id}", userId))
                .andExpect(status().isOk());

        verify(userService).deleteUser(userId);
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    public void deleteUser_WhenUserDoesNotExist() throws Exception {

        Long userId = 2L;

        when(userService.getUserById(userId)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/user/{id}", userId))
                .andExpect(status().isNotFound());

        verify(userService, never()).deleteUser(anyLong());
    }

}

