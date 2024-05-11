package com.desiato.puresynth.services;

import com.desiato.puresynth.controllers.UserController;
import com.desiato.puresynth.models.User;
import com.desiato.puresynth.repositories.SessionRepository;
import com.desiato.puresynth.repositories.UserRepository;
import io.swagger.v3.oas.models.info.Contact;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SessionRepository sessionRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public void deleteUserSessions(Long userId) {
        logger.info("Deleting sessions for user ID: {}", userId);
        sessionRepository.deleteByUserId(userId);
        logger.info("Deleted sessions for user ID: {}", userId);
    }

    public User createUser(String email, String password) {
        User newUser = new User(email, password);
        userRepository.save(newUser);
        return newUser;
    }

}
