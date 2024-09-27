package com.desiato.puresynth.services;

import com.desiato.puresynth.configurations.ProjectConfig;
import com.desiato.puresynth.dtos.UserRequestDTO;
import com.desiato.puresynth.models.User;
import com.desiato.puresynth.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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
    private final ProjectConfig projectConfig;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User getUserByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + id + " was not found"));
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
        User newUser = new User(email, projectConfig.passwordEncoder().encode(password));
        return userRepository.save(newUser);
    }

    public User updateUserOrThrow(Long id, UserRequestDTO userRequestDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + "not found"));

        existingUser.setEmail(userRequestDTO.email());
        existingUser.setPassword(projectConfig.passwordEncoder().encode(userRequestDTO.password()));

        return userRepository.save(existingUser);
    }
}
