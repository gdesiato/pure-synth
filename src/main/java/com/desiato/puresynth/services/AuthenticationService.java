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
        User user = findUserOrThrow(request);
        validatePassword(request, user);

        Session session = sessionService.createSession(user);

        String tokenValue = session.getToken();

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
