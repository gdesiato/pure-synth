package com.desiato.puresynth.repositories;

import com.desiato.puresynth.models.User;
import com.desiato.puresynth.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    public void testFindByEmail() {
        User createUser = userService.createUser("test@example.com", "password123");
        userRepository.save(createUser);
        User user = userRepository.findByEmail("test@example.com");
        assertNotNull(user);
    }
}
