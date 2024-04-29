package com.desiato.puresynth.services;

import com.desiato.puresynth.models.User;
import com.desiato.puresynth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Stores active sessions, typically this should be in a more persistent or distributed store
    private Map<String, String> activeSessions = new ConcurrentHashMap<>();

    public Optional<String> authenticate(String email, String password) {
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByEmail(email));
        if (optionalUser.isEmpty() || !passwordEncoder.matches(password, optionalUser.get().getPassword())) {
            return Optional.empty();
        }

        // Generate token and store it with user's email
        String token = UUID.randomUUID().toString();
        activeSessions.put(token, email); // In production, consider a more secure token management system
        return Optional.of(token);
    }

    public boolean isUserAuthenticated(String token) {
        return activeSessions.containsKey(token);
    }
}
