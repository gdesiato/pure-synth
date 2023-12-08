package com.desiato.puresynth.controllers;

import com.desiato.puresynth.models.User;
import com.desiato.puresynth.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @Autowired
    UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

//    @PostMapping("/login")
//    public String loginUser(@ModelAttribute User user, HttpSession session) {
//        User authenticatedUser = userService.findByUsernameAndPassword(user.getUsername(), user.getPassword());
//        if (authenticatedUser != null) {
//            session.setAttribute("currentUser", authenticatedUser);
//            // Redirect to the user's specific page
//            return "redirect:/user/" + authenticatedUser.getId();
//        } else {
//            // Login failed
//            return "login";
//        }
//    }

    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }

    @GetMapping("/logout-success")
    public String logout() {
        return "logout";
    }
}
