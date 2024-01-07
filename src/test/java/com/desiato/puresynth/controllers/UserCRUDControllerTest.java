package com.desiato.puresynth.controllers;

import com.desiato.puresynth.models.User;
import com.desiato.puresynth.repositories.RoleRepository;
import com.desiato.puresynth.repositories.UserRepository;
import com.desiato.puresynth.restControllers.UserCRUDController;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;



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


    @Test
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
    public void testRegisterUser() throws Exception {
        User newUser = new User();
        newUser.setUsername("newUser");
        newUser.setEmail("newUser@test.com");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("newUser");
        savedUser.setEmail("newUser@test.com");

        when(userService.findByUsername("newUser")).thenReturn(null);
        when(userService.saveUser(any(User.class))).thenReturn(savedUser);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("newUser")))
                .andExpect(jsonPath("$.email", is("newUser@test.com")));
    }
}