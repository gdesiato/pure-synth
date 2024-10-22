package com.desiato.puresynth.controllers;

import com.desiato.puresynth.BaseTest;
import com.desiato.puresynth.dtos.AuthenticatedUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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

        MvcResult result = mockMvc.perform(post("/api/audio")
                        .header("Authorization", "Bearer " +
                                authenticatedUser.pureSynthToken().value())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(audioJson))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        String expectedFileName = String.format("sine_wave_%.2fHz.wav", frequency);

        assertNotNull(responseBody, "The response body should not be null");
        assertTrue(responseBody.contains("File generated:"),
                "The response should contain the file generation confirmation");
        assertTrue(responseBody.contains(expectedFileName),
                "The response should contain the correct file name");
    }

    @Test
    void retrieve_ShouldRetrieveSavedFile() throws Exception {
        double frequency = Math.random() * 10000 + 200;

        String audioJson = String.format("""
        {
            "frequency" : %.2f,
            "duration" : 1
        }
        """, frequency);

        MvcResult result = mockMvc.perform(post("/api/audio")
                        .header("Authorization", "Bearer " +
                                authenticatedUser.pureSynthToken().value())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(audioJson))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        String fileName = extractFileNameFromResponse(responseBody);

        String expectedFileName = String.format("sine_wave_%.2fHz.wav", frequency);
        assertEquals(expectedFileName, fileName, "The file name should match the expected pattern");

        MvcResult fileResult = mockMvc.perform(get("/api/audio/{fileName}", fileName)
                        .header("Authorization", "Bearer " +
                                authenticatedUser.pureSynthToken().value()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andReturn();

        byte[] audioContent = fileResult.getResponse().getContentAsByteArray();
        assertNotNull(audioContent, "The audio file content should not be null");
        assertTrue(audioContent.length > 0, "The audio file should not be empty");
    }

    private String extractFileNameFromResponse(String responseBody) {
        return responseBody.replace("File generated: ", "").trim();
    }
}
