package com.desiato.puresynth.services;

import com.desiato.puresynth.models.Session;
import com.desiato.puresynth.models.User;
import com.desiato.puresynth.repositories.SessionRepository;
import com.desiato.puresynth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

        if (!passwordEncoder.matches(password, optionalUser.get().getPassword())) {
            logger.warn("Authentication failed: Incorrect password for email: {}", email);
            return Optional.empty();
        }

        logger.info("Authentication succeeded for email: {}", email);

        String token = UUID.randomUUID().toString();

        User user = optionalUser.get();

        // Set login time
        user.setLoginTime(System.currentTimeMillis());

        // Create session
        Session session = new Session(token, email, user, user.getLoginTime());

        // Save session
        sessionRepository.save(session);

        logger.info("Session created for email: {} with token: {}", email, token);

        return Optional.of(token);
    }

    public boolean isUserAuthenticated(String token) {
        Optional<Session> sessionOpt = sessionRepository.findById(token);

        if (sessionOpt.isEmpty()) {
            logger.warn("Token authentication failed: Session not found for token {}", token);
            return false;
        }

        Session session = sessionOpt.get();

        User user = session.getUser();  // Retrieve user from session

        if (user == null) {
            logger.warn("Token authentication failed: No user found in session");
            return false;
        }

        long userLoginTime = user.getLoginTime();

        // Check if session login timestamp is within a reasonable window of user login time
        long currentTime = System.currentTimeMillis();
        long maxValidTimeDiff = 500;

        if (Math.abs(currentTime - session.getLoginTimestamp()) <= maxValidTimeDiff) {
            return true;
        } else {
            logger.warn("Token authentication failed: Login timestamp mismatch for session");
            return false;
        }
    }
}
