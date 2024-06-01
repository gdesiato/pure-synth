package com.desiato.puresynth.controllers;

import com.desiato.puresynth.dtos.UserResponseDTO;
import com.desiato.puresynth.exceptions.InvalidTokenException;
import com.desiato.puresynth.models.CustomUserDetails;
import com.desiato.puresynth.models.User;
import com.desiato.puresynth.services.AuthenticationService;
import com.desiato.puresynth.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;

    public UserController(UserService userService, PasswordEncoder passwordEncoder, AuthenticationService authService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationService = authService;
    }

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/me")
    public ResponseEntity<?> getUserDetailsByToken(@RequestHeader("authToken") String token) {
        Optional<CustomUserDetails> customUserDetailsOpt = authenticationService.loadUserByToken(token);

        if (customUserDetailsOpt.isPresent()) {
            CustomUserDetails customUserDetails = customUserDetailsOpt.get();
            User user = customUserDetails.user();
            UserResponseDTO userDto = new UserResponseDTO(user.getId(), user.getEmail());
            return ResponseEntity.ok(userDto);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody User newUser) {
        if (userService.findByEmail(newUser.getEmail()) != null) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("User already exists");
        }
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        User createdUser = userService.saveUser(newUser);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        return userService.getUserById(id)
                .map(user -> {user.setEmail(userDetails.getEmail());
                    User updatedUser = userService.saveUser(user);
                    return ResponseEntity.ok(updatedUser);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(user -> {userService.deleteUser(id);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
