package com.desiato.puresynth.services;

import com.desiato.puresynth.models.User;
import com.desiato.puresynth.repositories.SessionRepository;
import com.desiato.puresynth.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final SessionService sessionService;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            sessionService.deleteByUserId(id);
            userRepository.deleteById(id);
        }
    }

    public User createUser(String email, String password) {
        User newUser = new User(email, password);
        userRepository.save(newUser);
        return newUser;
    }
}
