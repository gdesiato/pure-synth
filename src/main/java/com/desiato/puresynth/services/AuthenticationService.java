package com.desiato.puresynth.services;

import com.desiato.puresynth.dtos.AuthenticationRequestDTO;
import com.desiato.puresynth.dtos.PureSynthToken;
import com.desiato.puresynth.models.CustomUserDetails;
import com.desiato.puresynth.models.Session;
import com.desiato.puresynth.models.User;
import com.desiato.puresynth.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SessionService sessionService;

    public PureSynthToken authenticate(AuthenticationRequestDTO request) {
        log.info("Attempting to authenticate user with email: {}", request.email());

        User user;
        try {
            user = findUserOrThrow(request);
            log.info("User found: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Error finding user: {}", e.getMessage(), e);
            throw e;
        }

        try {
            validatePassword(request, user);
            log.info("Password validation successful for email: {}", request.email());
        } catch (Exception e) {
            log.error("Error validating password: {}", e.getMessage(), e);
            throw e;
        }

        Session session;
        try {
            session = sessionService.createSession(user);
            log.info("Session created for user with email: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Error creating session: {}", e.getMessage(), e);
            throw e;
        }

        String tokenValue = session.getToken();
        log.info("Authentication succeeded and token generated for email: {}", request.email());

        return new PureSynthToken(tokenValue);
    }

    public Optional<CustomUserDetails> createUserDetails(PureSynthToken pureSynthToken) {
        return sessionService.findUserByToken(pureSynthToken)
                .map(CustomUserDetails::new);
    }

    public boolean isUserAuthenticated(PureSynthToken pureSynthToken) {
        return sessionService.findUserByToken(pureSynthToken).isPresent();
    }

    private void validatePassword(AuthenticationRequestDTO request, User user) {
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            log.warn("Authentication failed: Incorrect password for email: {}", request.email());
            throw new AuthenticationException("Authentication failed: Incorrect password.") {};
        }
    }

    private User findUserOrThrow(AuthenticationRequestDTO request) {
        return userRepository.findByEmail(request.email())
                .orElseThrow(() -> {
                    return new AuthenticationException("Authentication failed: No user found.") {};
                });
    }
}
