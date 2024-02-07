package com.desiato.puresynth.controllers;

import com.desiato.puresynth.models.User;
import com.desiato.puresynth.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserCRUDController {

    private static final Logger logger = LoggerFactory.getLogger(UserCRUDController.class);

    @Autowired
    private UserService userService;

    @GetMapping
    public String helloApi(){
        return "hello api";
    }

    @GetMapping("/info")
    public String userInfo() {
        // Fetching the current authenticated user's details
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); // Gets the username of the authenticated user

        return "Access granted for user: " + username;
    }

    // Create a new user (open endpoint) - register yourself
    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody User newUser) {
        if (userService.findByUsername(newUser.getUsername()) != null) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Username already exists");
        }
        User registeredUser = userService.saveUser(newUser);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    // Fetch the logged-in user's profile
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserProfile(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update the logged-in user's profile
    @PutMapping("/{id}")
    public ResponseEntity<User> updateMyProfile(@PathVariable Long id, @RequestBody User userDetails, Authentication authentication) {
        logger.info("Update Profile Request: UserID = {}, UserDetails = {}", id, userDetails);

        // Check if the authenticated user has the 'ROLE_USER' authority
        boolean hasUserRole = false;
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if ("ROLE_USER".equals(authority.getAuthority())) {
                hasUserRole = true;
                break;
            }
        }

        if (!hasUserRole) {
            logger.warn("User does not have ROLE_USER authority");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        String username = authentication.getName();
        Optional<User> optionalUser = userService.getUserById(id);

        if (!optionalUser.isPresent()) {
            logger.warn("User with ID {} not found", id);
            return ResponseEntity.notFound().build();
        }

        User user = optionalUser.get();
        if (!user.getUsername().equals(username)) {
            logger.warn("Authenticated user's username does not match the requested user's username");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Update the user details
        logger.info("Updating user details for user: {}", username);
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());

        // Save the updated user
        User updatedUser = userService.saveUser(user);
        logger.info("User details updated successfully for user: {}", username);

        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProfileById(@PathVariable Long id) {
        Optional<User> userOptional = userService.getUserById(id);
        if (userOptional.isPresent()) {
            userService.deleteUser(userOptional.get().getId());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}