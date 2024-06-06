package com.desiato.puresynth.services;

import com.desiato.puresynth.dtos.AuthenticationRequestDTO;
import com.desiato.puresynth.dtos.PureSynthToken;
import com.desiato.puresynth.models.CustomUserDetails;
import com.desiato.puresynth.models.Session;
import com.desiato.puresynth.models.User;
import com.desiato.puresynth.repositories.SessionRepository;
import com.desiato.puresynth.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final SessionRepository sessionRepository;

    AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.sessionRepository = sessionRepository;
    }


    public PureSynthToken authenticate(AuthenticationRequestDTO request) throws AuthenticationException {
        log.info("Attempting to authenticate user with email: {}", request.email());

        User user = userRepository.findByEmail(request.email());
        if (user == null) {
            log.warn("Authentication failed: No user found for email: {}", request.email());
            throw new AuthenticationException("Authentication failed: No user found.") {};
        }

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            log.warn("Authentication failed: Incorrect password for email: {}", request.email());
            throw new AuthenticationException("Authentication failed: Incorrect password.") {};
        }

        String tokenValue = UUID.randomUUID().toString();
        Session newSession = new Session(tokenValue, user.getId());
        sessionRepository.save(newSession);
        log.info("Authentication succeeded and session created for email: {}", request.email());

        return new PureSynthToken(tokenValue);
    }

    public boolean isUserAuthenticated(PureSynthToken pureSynthToken) {
        Optional<Session> sessionOpt = sessionRepository.findById(pureSynthToken.value());

        if (sessionOpt.isEmpty()) {
            log.warn("Token authentication failed: Session not found for token");
            return false;
        }

        Session session = sessionOpt.get();
        Long userId = session.getUserId();
        Optional<User> user = userRepository.findById(userId);

        if (user == null) {
            log.warn("Token authentication failed: No user found in session");
            return false;
        }

        return true;
    }

    public Optional<CustomUserDetails> findByToken(String token) {
        return sessionRepository.findById(token)
                .map(Session::getUserId)
                .flatMap(userRepository::findById)
                .map(CustomUserDetails::new);
    }

    @Transactional
    public void deleteUserSessions(Long userId) {
        log.info("Deleting sessions for user ID: {}", userId);
        sessionRepository.deleteByUserId(userId);
        log.info("Deleted sessions for user ID: {}", userId);
    }


}
