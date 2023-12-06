package com.desiato.puresynth.services;

import com.desiato.puresynth.models.SecurityUser;
import com.desiato.puresynth.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Loading user by username: {}", username);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found: {}", username);
            throw new UsernameNotFoundException("No user found with username: " + username);
        }

        logger.info("User found: {}", username);
        return new SecurityUser(user);
    }
}