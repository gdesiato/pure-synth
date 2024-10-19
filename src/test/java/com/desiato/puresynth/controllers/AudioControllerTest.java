package com.desiato.puresynth.controllers;

import com.desiato.puresynth.BaseTest;
import com.desiato.puresynth.dtos.AuthenticatedUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class AudioControllerTest extends BaseTest {

    private AuthenticatedUser authenticatedUser;

    @BeforeEach
    void setUp() throws Exception {
        authenticatedUser = testAuthenticationHelper.createAndAuthenticateUser();
    }

    @Test
    void generate_ShouldReturnFileSaveConfirmation() throws Exception {
        double frequency = Math.random() * 10000 + 200;

        String audioJson = String.format("""
            {
                "frequency" : %.2f,
                "duration" : 1
            }
            """, frequency);

        MvcResult result = mockMvc.perform(post("/api/audio/generate")
                        .header("Authorization", "Bearer " + authenticatedUser.pureSynthToken().value())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(audioJson))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        System.out.println("Response: " + responseBody);
    }
}
