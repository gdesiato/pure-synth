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
        Session newSession = new Session(tokenValue, user);
        sessionRepository.save(newSession);
        return newSession;
    }

    public Optional<User> findUserByToken(PureSynthToken pureSynthToken) {
        return sessionRepository.findByToken(pureSynthToken.value())
                .map(Session::getUser);
    }

    public void deleteByUserId(Long userId) {
        sessionRepository.deleteByUserId(userId);
    }
}
