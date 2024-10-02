package com.desiato.puresynth.validators;

import com.desiato.puresynth.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
class AuthenticationRequestValidatorTest extends BaseTest {

    String validEmail;
    String validPassword;

    @BeforeEach
    void setUp() {
        validEmail = testAuthenticationHelper.generateUniqueEmail();
        validPassword = "password123";
        userService.createUser(validEmail, validPassword);
    }

    @Test
    void authenticate_ShouldReturnValidationErrorWhenEmailIsInvalid() throws Exception {

        log.info("Starting test: authenticate_ShouldReturnValidationErrorWhenEmailIsInvalid");


        String authRequestJson = """
                {
                  "email": "invalidEmail",
                  "password": "password123"
                }
                """;

        log.info("Sending request with JSON: " + authRequestJson);


        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].message").value("Invalid email format."));

        log.info("Test: authenticate_ShouldReturnValidationErrorWhenEmailIsInvalid finished");

    }

    @Test
    void authenticate_ShouldReturnTokenWithValidRequest() throws Exception {

        log.info("Starting test: authenticate_ShouldReturnTokenWithValidRequest");

        String authRequestJson = String.format("""
                {
                  "email": "%s",
                  "password": "%s"
                }
                """, validEmail, validPassword);

        log.info("Sending request with JSON: " + authRequestJson);



        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authToken").exists())
                .andExpect(jsonPath("$.message").value("success"));

        log.info("Test: authenticate_ShouldReturnTokenWithValidRequest finished");

    }
}
