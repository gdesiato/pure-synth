package com.desiato.puresynth.configurations;

import com.desiato.puresynth.BaseTest;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;


import java.util.UUID;

@SpringBootTest
public class AuthTokenFilterTest extends BaseTest {

    @Test
    public void doFilterInternal_WithValidToken_ShouldAllowAccess() throws Exception {
        // Create a valid user and authenticate
        String uniqueEmail = generateUniqueEmail();
        String password = "password123";
        userService.createUser(uniqueEmail, passwordEncoder.encode(password));

        String json = """
            {
                "email": "%s",
                "password": "%s"
            }
            """.formatted(uniqueEmail, password);

        MvcResult loginResult = mockMvc.perform(post("/api/login/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(header().exists("Auth-Token"))
                .andReturn();

        String token = loginResult.getResponse().getHeader("Auth-Token");

        // Access a protected endpoint using the valid token
        mockMvc.perform(get("/api/protected")
                        .header("Auth-Token", token))
                .andExpect(status().isOk());
    }

    @Test
    public void doFilterInternal_WithInvalidToken_ShouldDenyAccess() throws Exception {
        // Use an invalid token directly
        mockMvc.perform(get("/api/protected")
                        .header("Auth-Token", "invalid_token"))
                .andExpect(status().isUnauthorized());
    }

    private String generateUniqueEmail() {
        return "user_" + UUID.randomUUID().toString() + "@example.com";
    }
}
