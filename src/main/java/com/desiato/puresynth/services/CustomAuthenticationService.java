package com.desiato.puresynth.services;

import com.desiato.puresynth.models.User;
import com.desiato.puresynth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CustomAuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean authenticate(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            logger.error("No user found with email: {}", email);
            return false;
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            logger.error("Invalid password for user: {}", email);
            return false;
        }
        logger.info("Successful authentication for user: {}", email);
        return true;
    }
}
