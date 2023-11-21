package com.desiato.puresynth.controllers;

import com.desiato.puresynth.models.User;
import com.desiato.puresynth.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

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

        // Save the new user
        userService.saveUser(newUser);

        return "redirect:/login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute User user, HttpSession session) {
        User authenticatedUser = userService.findByUsernameAndPassword(user.getUsername(), user.getPassword());
        if (authenticatedUser != null) {
            session.setAttribute("currentUser", authenticatedUser);
            // Redirect to the user's specific page
            return "redirect:/user/" + authenticatedUser.getId();
        } else {
            // Login failed
            return "login";
        }
    }

    @GetMapping("/user/{userId}")
    public String userPage(@PathVariable Long userId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (user != null && user.getId().equals(userId)) {
            model.addAttribute("user", user);
            return "userPage"; // user-specific page
        } else {
            return "redirect:/login"; // redirect to login if the user is not in session or IDs do not match
        }
    }
}

