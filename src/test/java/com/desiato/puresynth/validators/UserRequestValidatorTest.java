package com.desiato.puresynth.validators;

import com.desiato.puresynth.BaseTest;
import com.desiato.puresynth.dtos.AuthenticatedUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserRequestValidatorTest extends BaseTest {

    private AuthenticatedUser authenticatedUser;
    private String validEmail;

    @BeforeEach
    void setUp() throws Exception {
        authenticatedUser = testAuthenticationHelper.createAndAuthenticateUser();
        validEmail = testAuthenticationHelper.generateUniqueEmail();
    }

    @Test
    void createUser_ShouldReturnValidationErrorWhenEmailIsBlank() throws Exception {
        String userRequestJson = """
            {
              "email": "",
              "password": "password123"
            }
            """;

        mockMvc.perform(post("/api/user")
                        .header("authToken", authenticatedUser.pureSynthToken().value())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].message").value("Email cannot be blank."));
    }

    @Test
    void createUser_ShouldReturnValidationErrorWhenPasswordIsBlank() throws Exception {

        String userRequestJson = String.format("""
                {
                  "email": "%s",
                  "password": ""
                }
                """, validEmail);

        mockMvc.perform(post("/api/user")
                        .header("authToken", authenticatedUser.pureSynthToken().value())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].message").value("Password cannot be blank."));
    }

    @Test
    void createUser_ShouldSucceedWithValidRequest() throws Exception {
        String email = testAuthenticationHelper.generateUniqueEmail();

        String userRequestJson = String.format("""
                {
                  "email": "%s",
                  "password": "password123"
                }
                """, email);

        mockMvc.perform(post("/api/user")
                        .header("authToken", authenticatedUser.pureSynthToken().value())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(email));
    }
}
