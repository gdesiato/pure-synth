package com.desiato.puresynth.controllers;

import com.desiato.puresynth.BaseTest;
import com.desiato.puresynth.dtos.AuthenticatedUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


class AudioControllerTest extends BaseTest {

    private AuthenticatedUser authenticatedUser;

    @BeforeEach
    void setUp() throws Exception {
        authenticatedUser = testAuthenticationHelper.createAndAuthenticateUser();
    }

    @Test
    void generate_ShouldReturnAudioFile() throws Exception {
        String audioJson = """
                {
                    "frequency" : 501,
                    "duration" : 1000
                }
                """;

        mockMvc.perform(post("/api/audio/generate")
                        .header("Authorization", "Bearer " + authenticatedUser.pureSynthToken().value())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(audioJson))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition",
                        "attachment; filename=\"sine_wave_501.0Hz.wav\"")) // Check the header
                .andExpect(content().contentType("audio/wav"));
    }
}
