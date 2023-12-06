package com.desiato.puresynth.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserRestController {

    @GetMapping("/info")
    public String userInfo() {
        // Fetching the current authenticated user's details
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); // Gets the username of the authenticated user

        return "Access granted for user: " + username;
    }
}
