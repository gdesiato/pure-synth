package com.desiato.puresynth.controllers;

import com.desiato.puresynth.BaseTest;
import com.desiato.puresynth.dtos.AuthenticatedUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;

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
                    "duration" : 1
                }
                """;

        MvcResult result = mockMvc.perform(post("/api/audio/generate")
                        .header("Authorization", "Bearer " + authenticatedUser.pureSynthToken().value())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(audioJson))
                .andExpect(status().isOk())  // Expecting 200 OK
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"sine_wave_501.0Hz.wav\""))
                .andExpect(content().contentType("audio/wav"))
                .andReturn();

        // Get the response body as bytes
        MockHttpServletResponse response = result.getResponse();
        byte[] audioBytes = response.getContentAsByteArray();

        // Calculate the size of the generated file in bytes
        int fileSizeInBytes = audioBytes.length;

        System.out.println("Generated audio file size: " + fileSizeInBytes + " bytes");

    }
}
