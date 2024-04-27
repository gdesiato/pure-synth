package com.desiato.puresynth.configurations;

import com.desiato.puresynth.models.User;
import com.desiato.puresynth.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initData() {
        if (userRepository.findByEmail("user@mail.com") == null) {
            User user = new User();
            user.setEmail("user@mail.com");
            user.setPassword(passwordEncoder.encode("user"));
            userRepository.save(user);
        }
    }
}
