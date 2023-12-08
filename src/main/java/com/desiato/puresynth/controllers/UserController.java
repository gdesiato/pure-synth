package com.desiato.puresynth.controllers;

import com.desiato.puresynth.configurations.SecurityConfig;
import com.desiato.puresynth.models.Role;
import com.desiato.puresynth.models.SecurityUser;
import com.desiato.puresynth.models.User;
import com.desiato.puresynth.repositories.RoleRepository;
import com.desiato.puresynth.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    // Display registration form
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // Process registration form submission
    @PostMapping("/register")
    public String registerUser(@ModelAttribute User newUser, BindingResult result) {
        if (result.hasErrors()) {
            return "register";
        }

        if (userService.findByUsername(newUser.getUsername()) != null) {
            result.rejectValue("username", "error.user", "Username already exists");
            return "register";
        }

        // Encrypt the password
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        // Assign the Role and Save the new user
        Role defaultRole = roleRepository.findByName("ROLE_USER");
        newUser.setRoles(Collections.singleton(defaultRole));
        userService.saveUser(newUser);

        return "redirect:/login";
    }


    @GetMapping("/user/{userId}")
    public String userPage(@PathVariable Long userId, Model model, Authentication authentication) {
        logger.info("Accessing user page for ID: {}", userId);

        if (authentication != null && authentication.getPrincipal() instanceof SecurityUser securityUser) {
            User user = securityUser.getUser();

            if (user.getId().equals(userId)) {
                model.addAttribute("user", user);
                logger.info("User ID matches. Displaying user page for ID: {}", userId);
                return "userPage"; // user-specific page
            } else {
                logger.warn("User ID does not match the authenticated user's ID.");
            }
        } else {
            logger.warn("Authentication object is null or principal is not an instance of SecurityUser.");
        }

        logger.info("Redirecting to login page.");
        return "redirect:/login";
    }
}

