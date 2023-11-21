package com.desiato.puresynth.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebAudioViewController {

    @GetMapping("/webaudio")
    public String serveWebAudioPage() {
        return "webaudio";
    }
}

