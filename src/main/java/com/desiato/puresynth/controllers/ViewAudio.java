package com.desiato.puresynth.controllers;

import com.desiato.puresynth.models.SecurityUser;
import com.desiato.puresynth.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mvc")
public class ViewAudio {

    private static final Logger log = LoggerFactory.getLogger(ViewAudio.class);

    // open access endpoint
    @GetMapping("/login")
    public String login(){
        return "login";
    }

    // open access endpoint
    @GetMapping("/testing")
    public String testing(Model model) {
        model.addAttribute("message", "this works! yes :)");
        return "testView";
    }

    @GetMapping("/user")
    public String userProfile(Model model) {
        log.info("Attempting to access user profile page");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        if (principal instanceof SecurityUser) {
            SecurityUser securityUser = (SecurityUser) principal;
            User user = securityUser.getUser(); //User object

            log.info("User {} authenticated successfully, loading user page.", user.getUsername());
            model.addAttribute("user", user);
            return "userPage";
        } else {
            log.info("User principal is not instance of SecurityUser, redirecting to login");
            return "redirect:/mvc/login"; // Redirect to the login page if the user details are not found
        }
    }

    @GetMapping("/user-view")
    public String getUserView(Model model, Authentication authentication) {
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            model.addAttribute("username", userDetails.getUsername());
            model.addAttribute("roles", userDetails.getAuthorities());
        }
        return "user-view";
    }
    
}
