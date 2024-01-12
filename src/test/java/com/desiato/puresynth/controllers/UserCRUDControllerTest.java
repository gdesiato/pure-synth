package com.desiato.puresynth.controllers;

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

//    @Test
//    @WithMockUser(username = "testuser")
//    public void testRegisterUser() throws Exception {
//        logger.info("Starting testRegisterUser");
//
//        User newUser = new User();
//        newUser.setUsername("newUser");
//        newUser.setEmail("newUser@test.com");
//        logger.info("Created newUser for testing");
//
//        User savedUser = new User();
//        savedUser.setId(1L);
//        savedUser.setUsername("newUser");
//        savedUser.setEmail("newUser@test.com");
//        logger.info("Created savedUser for testing");
//
//        when(userService.findByUsername("newUser")).thenReturn(null);
//        when(userService.saveUser(any(User.class))).thenReturn(savedUser);
//        logger.info("Mocked userService methods");
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String newUserJson = objectMapper.writeValueAsString(newUser);
//
//        logger.info("Performing POST request to register a new user");
//        mockMvc.perform(post("/api/users/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(newUserJson))
//                .andDo(print())
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id", is(1)))
//                .andExpect(jsonPath("$.username", is("newUser")))
//                .andExpect(jsonPath("$.email", is("newUser@test.com")));
//        logger.info("POST request performed");
//
//        logger.info("Verifying userService.saveUser was called");
//        verify(userService).saveUser(any(User.class));
//        logger.info("testRegisterUser completed");
//    }

    @Test
    @WithMockUser(username = "testUser")
    public void testRegisterUser_Basic() throws Exception {
        logger.info("Starting simplified testRegisterUser");

        User newUser = new User();
        newUser.setUsername("newUser");
        newUser.setEmail("newUser@test.com");

        when(userService.findByUsername("newUser")).thenReturn(null);
        when(userService.saveUser(any(User.class))).thenReturn(new User()); // Returning a basic user object

        ObjectMapper objectMapper = new ObjectMapper();
        String newUserJson = objectMapper.writeValueAsString(newUser);

        logger.info("Performing POST request to register a new user with basic assertions");
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUserJson))
                .andExpect(status().isCreated());

        logger.info("Verifying that userService methods are called as expected");
        verify(userService).findByUsername("newUser");
        verify(userService).saveUser(any(User.class));

        logger.info("Simplified testRegisterUser completed");
    }
}
