package com.desiato.puresynth.validators;

import com.desiato.puresynth.BaseTest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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

        String authRequestJson = """
                {
                  "email": "invalidEmail",
                  "password": "password123"
                }
                """;

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].message").value("Invalid email format."));
    }

    @Test
    void authenticate_ShouldReturnTokenWithValidRequest() throws Exception {

        String authRequestJson = String.format("""
                {
                  "email": "%s",
                  "password": "%s"
                }
                """, validEmail, validPassword);

        log.info("Validating email: " + validEmail);


        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authToken").exists())
                .andExpect(jsonPath("$.message").value("success"));
    }
}
