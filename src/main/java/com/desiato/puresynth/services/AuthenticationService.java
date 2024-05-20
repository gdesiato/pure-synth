package com.desiato.puresynth.services;

import com.desiato.puresynth.exceptions.InvalidTokenException;
import com.desiato.puresynth.models.CustomUserDetails;
import com.desiato.puresynth.models.Session;
import com.desiato.puresynth.models.User;
import com.desiato.puresynth.repositories.SessionRepository;
import com.desiato.puresynth.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;


@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SessionRepository sessionRepository;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public Optional<String> authenticate(String email, String password) {
        logger.info("Attempting to authenticate user with email: {}", email);

        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByEmail(email));

        if (optionalUser.isEmpty()) {
            logger.warn("Authentication failed: No user found for email: {}", email);
            return Optional.empty();
        }

        User user = optionalUser.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            logger.warn("Authentication failed: Incorrect password for email: {}", email);
            return Optional.empty();
        }

        String token = UUID.randomUUID().toString();
        Session session = new Session(token, email, user);
        sessionRepository.save(session);
        logger.info("Authentication succeeded and session created for email: {}", email);

        return Optional.of(token);
    }

    public boolean isUserAuthenticated(String token) {
        Optional<Session> sessionOpt = sessionRepository.findById(token);

        if (sessionOpt.isEmpty()) {
            logger.warn("Token authentication failed: Session not found for token {}", token);
            return false;
        }

        Session session = sessionOpt.get();
        User user = session.getUser();

        if (user == null) {
            logger.warn("Token authentication failed: No user found in session");
            return false;
        }

        return true;
    }

    public UserDetails loadUserByToken(String token) throws InvalidTokenException {
        Optional<Session> sessionOpt = sessionRepository.findById(token);
        if (sessionOpt.isPresent()) {
            Session session = sessionOpt.get();
            User user = session.getUser();
            if (user != null) {
                return new CustomUserDetails(user);
            } else {
                throw new InvalidTokenException("Invalid token: associated user is null");
            }
        } else {
            throw new InvalidTokenException("Invalid token: token not found");
        }
    }

    public Optional<UserDetails> authenticateByToken(String token) throws InvalidTokenException {
        if (token == null) {
            return Optional.empty();
        }
        if (!isUserAuthenticated(token)) {
            return Optional.empty();
        }
        return Optional.ofNullable(loadUserByToken(token));
    }

    @Transactional
    public void deleteUserSessions(Long userId) {
        logger.info("Deleting sessions for user ID: {}", userId);
        sessionRepository.deleteByUserId(userId);
        logger.info("Deleted sessions for user ID: {}", userId);
    }


}
