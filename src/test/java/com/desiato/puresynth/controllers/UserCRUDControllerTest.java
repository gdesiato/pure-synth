package com.desiato.puresynth.controllers;

import com.desiato.puresynth.models.Role;
import com.desiato.puresynth.models.User;
import com.desiato.puresynth.repositories.RoleRepository;
import com.desiato.puresynth.repositories.UserRepository;
import com.desiato.puresynth.restControllers.UserCRUDController;
import com.desiato.puresynth.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@WebMvcTest(UserCRUDController.class)
public class UserCRUDControllerTest {

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

    private static final Logger logger = LoggerFactory.getLogger(UserCRUDControllerTest.class);



    @Test
    @WithMockUser(username = "testuser")
    public void testHelloApi() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().string("hello api"));
    }

    @Test
    @WithMockUser(username = "testuser")
    public void testUserInfo() throws Exception {
        mockMvc.perform(get("/api/users/info"))
                .andExpect(status().isOk())
                .andExpect(content().string("Access granted for user: testuser"));
    }

    @Test
    @WithMockUser(username = "testUser")
    public void testRegisterUser_Basic() throws Exception {
        logger.info("Starting simplified testRegisterUser");

        User newUser = new User();
        newUser.setUsername("newUser");
        newUser.setEmail("newUser@test.com");
        newUser.setPassword("newPassword");

        Role userRole = new Role();
        userRole.setName("USER");
        newUser.setRoles(Collections.singleton(userRole));

        when(userService.findByUsername("newUser")).thenReturn(null);
        when(userService.saveUser(any(User.class))).thenReturn(new User()); // Returning a basic user object

        ObjectMapper objectMapper = new ObjectMapper();
        String newUserJson = objectMapper.writeValueAsString(newUser);
        String expectedUserJson = objectMapper.writeValueAsString(newUser);

        logger.info("Performing POST request to register a new user with basic assertions");
        // Performing the request
        MvcResult result = mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUserJson))
                .andReturn();

        // Logging the response details
        int status = result.getResponse().getStatus();
        String actualResponseContent = result.getResponse().getContentAsString();
        logger.info("Response status: " + status);
        logger.info("Response body: " + actualResponseContent);

        // Asserting the response status and content
        assertEquals(201, status, "Expected 201 Created but got " + status);
        assertEquals(expectedUserJson, actualResponseContent, "The response content does not match the expected user details.");


        logger.info("Verifying that userService methods are called as expected");
        verify(userService).findByUsername("newUser");
        verify(userService).saveUser(any(User.class));

        logger.info("Simplified testRegisterUser completed");
    }
}
