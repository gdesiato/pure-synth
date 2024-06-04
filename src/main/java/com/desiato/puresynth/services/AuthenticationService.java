package com.desiato.puresynth.services;

import com.desiato.puresynth.dtos.AuthenticationRequestDTO;
import com.desiato.puresynth.dtos.PureSynthToken;
import com.desiato.puresynth.models.CustomUserDetails;
import com.desiato.puresynth.models.Session;
import com.desiato.puresynth.models.User;
import com.desiato.puresynth.repositories.SessionRepository;
import com.desiato.puresynth.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SessionRepository sessionRepository;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public Optional<PureSynthToken> authenticate(AuthenticationRequestDTO request) {
        logger.info("Attempting to authenticate user with email: {}", request.email());

        User user = userRepository.findByEmail(request.email());
        if (user == null) {
            logger.warn("Authentication failed: No user found for email: {}", request.email());
            return Optional.empty();
        }

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            logger.warn("Authentication failed: Incorrect password for email: {}", request.email());
            return Optional.empty();
        }

        String tokenValue = UUID.randomUUID().toString();
        Session newSession = new Session(tokenValue, user.getId());
        sessionRepository.save(newSession);
        logger.info("Authentication succeeded and session created for email: {}", request.email());

        return Optional.of(new PureSynthToken(tokenValue));
    }

    public boolean isUserAuthenticated(PureSynthToken pureSynthToken) {
        Optional<Session> sessionOpt = sessionRepository.findById(pureSynthToken.value());

        if (sessionOpt.isEmpty()) {
            logger.warn("Token authentication failed: Session not found for token");
            return false;
        }

        Session session = sessionOpt.get();
        Long userId = session.getUserId();
        Optional<User> user = userRepository.findById(userId);

        if (user == null) {
            logger.warn("Token authentication failed: No user found in session");
            return false;
        }

        return true;
    }

    public Optional<CustomUserDetails> loadUserByToken(String token) {
        return sessionRepository.findById(token)
                .map(Session::getUserId)
                .flatMap(userRepository::findById)
                .map(CustomUserDetails::new);
    }

    @Transactional
    public void deleteUserSessions(Long userId) {
        logger.info("Deleting sessions for user ID: {}", userId);
        sessionRepository.deleteByUserId(userId);
        logger.info("Deleted sessions for user ID: {}", userId);
    }


}
