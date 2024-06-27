package com.desiato.puresynth.services;

import com.desiato.puresynth.dtos.PureSynthToken;
import com.desiato.puresynth.models.Session;
import com.desiato.puresynth.models.User;
import com.desiato.puresynth.repositories.SessionRepository;
import com.desiato.puresynth.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Slf4j
@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    public Session createSession(User user) {
        String tokenValue = UUID.randomUUID().toString();
        Session newSession = new Session(tokenValue, user.getId());
        log.info("Token to be assigned: {}", tokenValue);
        log.info("User ID to be assigned: {}", user.getId());
        try {
            sessionRepository.save(newSession);
            log.info("Token of newSession: " + newSession.getToken());
        } catch (Exception e){
            log.error("Error saving session: " + e.getMessage(), e);
            throw e;
        }
        return newSession;
    }

    public Optional<User> findUserByToken(PureSynthToken pureSynthToken) {
        return sessionRepository.findById(pureSynthToken.value())
                .map(Session::getUserId)
                .flatMap(userRepository::findById);
    }

    public void deleteUserSessions(Long userId) {
        sessionRepository.deleteByUserId(userId);
    }
}
