package com.desiato.tuningsystems.controllers;

import com.desiato.tuningsystems.models.User;
import com.desiato.tuningsystems.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User newUser) {
        if (userService.findByUsername(newUser.getUsername()) != null) {
            return ResponseEntity.badRequest().body(null); // Username already exists
        }
        User registeredUser = userService.saveUser(newUser);
        return ResponseEntity.ok(registeredUser);
    }

    // Fetch the logged-in user's profile
    @GetMapping("/me")
    public ResponseEntity<User> getMyProfile(Principal principal) {
        return Optional.ofNullable(userService.findByUsername(principal.getName()))
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }

    // Update the logged-in user's profile
    @PutMapping("/me")
    public ResponseEntity<User> updateMyProfile(@RequestBody User userDetails, Principal principal) {
        return Optional.ofNullable(userService.findByUsername(principal.getName())).map(user -> {
            user.setUsername(userDetails.getUsername());
            user.setEmail(userDetails.getEmail());
            // ... (update other properties)
            User updatedUser = userService.saveUser(user);
            return ResponseEntity.ok(updatedUser);
        }).orElse(ResponseEntity.notFound().build());
    }

    // User might delete their own account
    @DeleteMapping("/me")
    public ResponseEntity<?> deleteMyProfile(Principal principal) {
        return Optional.ofNullable(userService.findByUsername(principal.getName())).map(user -> {
            userService.deleteUser(user.getId());
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}