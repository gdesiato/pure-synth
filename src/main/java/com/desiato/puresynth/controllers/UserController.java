package com.desiato.puresynth.controllers;

import com.desiato.puresynth.dtos.PureSynthToken;
import com.desiato.puresynth.dtos.UserResponseDTO;
import com.desiato.puresynth.models.User;
import com.desiato.puresynth.services.SessionService;
import com.desiato.puresynth.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final SessionService sessionService;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getUserDetailsByToken(@RequestHeader("authToken") PureSynthToken pureSynthToken) {
        return sessionService.findUserByToken(pureSynthToken)
                .map(user -> ResponseEntity.ok(new UserResponseDTO(user.getId(), user.getEmail())))
                .orElseThrow(() -> new BadCredentialsException("Invalid or expired token."));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody User newUser) {
        if (userService.findByEmail(newUser.getEmail()).isPresent()) {
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
