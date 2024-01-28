package com.desiato.puresynth.restControllers;

import com.desiato.puresynth.models.User;
import com.desiato.puresynth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserCRUDController {

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
        String username = authentication.getName();
        Optional<User> optionalUser = userService.getUserById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Check if the user matches the authenticated user
            if (!user.getUsername().equals(username)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // Update the user details
            user.setUsername(userDetails.getUsername());
            user.setEmail(userDetails.getEmail());

            // Save the updated user
            User updatedUser = userService.saveUser(user);

            // Return the updated user details
            return ResponseEntity.ok(updatedUser);
        } else {
            // Return not found status if user with the given id doesn't exist
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/me")
    public ResponseEntity<?> deleteMyProfile(Principal principal) {
        return Optional.ofNullable(userService.findByUsername(principal.getName())).map(user -> {
            userService.deleteUser(user.getId());
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}