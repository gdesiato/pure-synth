package com.desiato.tuningsystems.services;

import org.springframework.stereotype.Service;

@Service
public class PdService {

    public void startAudio() {
        PdBase.openAudio(1, 2, 44100);
        PdBase.initReverb(true);
    }
}
