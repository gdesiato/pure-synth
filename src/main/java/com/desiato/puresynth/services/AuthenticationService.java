package com.desiato.puresynth.services;

import com.desiato.puresynth.dtos.AuthenticationRequestDTO;
import com.desiato.puresynth.dtos.Token;
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

    public Optional<Token> authenticate(AuthenticationRequestDTO request) {
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
        sessionRepository.save(new Session(tokenValue, user.getEmail(), user));
        logger.info("Authentication succeeded and session created for email: {}", request.email());

        return Optional.of(new Token(tokenValue));
    }

    public boolean isUserAuthenticated(Token token) {
        Optional<Session> sessionOpt = sessionRepository.findById(token.value());

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

    public CustomUserDetails loadUserByToken(String token) throws InvalidTokenException {
        Optional<Session> sessionOpt = sessionRepository.findById(token);
        if (sessionOpt.isPresent()) {
            Session session = sessionOpt.get();
            User user = session.getUser();
            if (user != null) {
                return new CustomUserDetails(user); // Directly return CustomUserDetails
            } else {
                throw new InvalidTokenException("Invalid token: associated user is null");
            }
        } else {
            throw new InvalidTokenException("Invalid token: token not found");
        }
    }

    public Optional<UserDetails> authenticateByToken(Token token) throws InvalidTokenException {
        if (token == null || token.value() == null) {
            throw new InvalidTokenException("Token is null.");
        }
        if (!isUserAuthenticated(token)) {
            throw new InvalidTokenException("Token is not valid or has expired.");
        }

        UserDetails userDetails = loadUserByToken(token.value());
        return Optional.ofNullable(userDetails);
    }

    @Transactional
    public void deleteUserSessions(Long userId) {
        logger.info("Deleting sessions for user ID: {}", userId);
        sessionRepository.deleteByUserId(userId);
        logger.info("Deleted sessions for user ID: {}", userId);
    }


}
