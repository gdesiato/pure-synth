package com.desiato.puresynth.services;

import com.desiato.puresynth.BaseTest;
import com.desiato.puresynth.models.Session;
import com.desiato.puresynth.models.User;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class SessionServiceTest extends BaseTest {

    @Test
    void createSession_ShouldReturnCreatedSession() throws Exception {

        User existingUser = testAuthenticationHelper.createAndPersistUser();
        Session createdSession = sessionService.createSession(existingUser);

        assertNotNull(createdSession, "Session is not be null");
        assertNotNull(createdSession.getId(), "Session ID should be set after saving");

    }
}
