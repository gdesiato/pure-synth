package com.desiato.puresynth.services;

import com.desiato.puresynth.dtos.AuthenticationRequestDTO;
import com.desiato.puresynth.dtos.PureSynthToken;
import com.desiato.puresynth.models.CustomUserDetails;
import com.desiato.puresynth.models.Session;
import com.desiato.puresynth.models.User;
import com.desiato.puresynth.repositories.SessionRepository;
import com.desiato.puresynth.repositories.UserRepository;
import jakarta.transaction.Transactional;
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
    private final SessionRepository sessionRepository;
    private final SessionService sessionService;

    public PureSynthToken authenticate(AuthenticationRequestDTO request) {
        log.info("Attempting to authenticate user with email: {}", request.email());

        User user = findUserOrThrow(request);

        validatePassword(request, user);

        Session session = sessionService.createSession(user);
        String tokenValue = session.getToken();
        log.info("Authentication succeeded and session created for email: {}", request.email());

        return new PureSynthToken(tokenValue);
    }

    private void validatePassword(AuthenticationRequestDTO request, User user) {
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            log.warn("Authentication failed: Incorrect password for email: {}", request.email());
            throw new AuthenticationException("Authentication failed: Incorrect password.") {};
        }
    }

    private User findUserOrThrow(AuthenticationRequestDTO request) {
        User user = userRepository.findByEmail(request.email());
        if (user == null) {
            log.warn("Authentication failed: No user found for email: {}", request.email());
            throw new AuthenticationException("Authentication failed: No user found.") {};
        }
        return user;
    }

    public Optional<CustomUserDetails> createUserDetails(PureSynthToken pureSynthToken) {
        return sessionService.findUserByToken(pureSynthToken)
                .map(CustomUserDetails::new);
    }

    public boolean isUserAuthenticated(PureSynthToken pureSynthToken) {
        return sessionService.findUserByToken(pureSynthToken).isPresent();
    }
}
