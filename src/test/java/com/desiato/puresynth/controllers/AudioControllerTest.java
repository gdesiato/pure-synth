package com.desiato.puresynth.controllers;

import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.desiato.puresynth.configurations.SecurityConfig;
import com.desiato.puresynth.models.AudioRequest;
import com.desiato.puresynth.repositories.RoleRepository;
import com.desiato.puresynth.repositories.UserRepository;
import com.desiato.puresynth.services.AudioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;

@WebMvcTest(AudioController.class)
@Import(SecurityConfig.class)
public class AudioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private AudioService audioService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void generateAudioSuccess() throws Exception {

        AudioRequest request = new AudioRequest();
        request.setFrequency(440.0);
        request.setDuration(5.0);
        File mockFile = new File("generatedAudio.wav");
        String expectedDownloadUrl = "http://localhost:8080/audio/generatedAudio.wav";

        given(audioService.generateSineWaveFile(anyDouble(), anyDouble())).willReturn(mockFile);


        mockMvc.perform(post("/audio/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedDownloadUrl));
    }

    @Test
    void generateAudioFailure() throws Exception {

        AudioRequest request = new AudioRequest();
        request.setFrequency(440.0);
        request.setDuration(5.0);
        given(audioService.generateSineWaveFile(anyDouble(), anyDouble())).willThrow(new RuntimeException("Test Exception"));


        mockMvc.perform(post("/audio/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error generating audio: Test Exception"));
    }
}