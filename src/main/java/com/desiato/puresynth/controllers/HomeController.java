package com.desiato.puresynth.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/homepage")
    public String homePage() {
        return "You are logged in! <br>" +
                "What a joy...";
    }
}
