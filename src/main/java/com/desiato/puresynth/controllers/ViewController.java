package com.desiato.puresynth.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ViewController {

    @GetMapping("/frequency")
    public ModelAndView serveTemplate() {
        return new ModelAndView("synth");
    }
}