package com.desiato.puresynth.services;

import com.desiato.puresynth.BaseTest;
import com.desiato.puresynth.dtos.AuthenticatedUser;
import com.desiato.puresynth.models.Session;
import com.desiato.puresynth.models.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserServiceTest extends BaseTest {

    @Test
    @Transactional
    public void deleteUser_ShouldDeleteUserAndSession() throws Exception {
        AuthenticatedUser authenticatedUser = testAuthenticationHelper.createAndAuthenticateUser();
        Long userId = authenticatedUser.user().getId();
        String token = authenticatedUser.pureSynthToken().value();

        Optional<Session> userSessionOpt = sessionRepository.findByToken(token);

        userService.deleteUser(userId);

        Optional<User> deletedUser = userRepository.findById(userId);
        Optional<Session> deletedSession = sessionRepository.findByToken(token);

        assertThat(deletedUser.isPresent()).isFalse();
        assertThat(deletedSession.isPresent()).isFalse();
    }
}
