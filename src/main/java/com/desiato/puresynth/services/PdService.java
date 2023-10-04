package com.desiato.puresynth.services;

import com.desiato.puresynth.models.PdReceiverAdapter;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.puredata.core.PdBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;


@Service
public class PdService {

    private static final Logger logger = LoggerFactory.getLogger(PdService.class);

    @Autowired
    private PdReceiverAdapter pdReceiverAdapter;

    private int patchHandle;
    private String patchPath;

    public PdService(@Value("${puredata.patch.path}") String patchPath) {
        this.patchPath = patchPath;
    }

    @PostConstruct
    public void init() {
        try {
            logger.info("Initializing PdService");

            try {
                logger.info("Attempting to open audio with libpd...");
                PdBase.openAudio(1, 2, 44100);
                logger.info("Audio opened successfully with libpd.");
            } catch (Exception e) {
                logger.error("Failed to open audio with libpd.", e);
                throw e;
            }

            // Set a receiver to handle messages from Pd.
            logger.info("Setting receiver for PdBase");
            PdBase.setReceiver(new PdReceiverAdapter());

            // Load a patch.
            logger.info("Loading patch from path: {}", patchPath);
            File patchFile = new File(patchPath);
            patchHandle = PdBase.openPatch(patchFile.getAbsolutePath());
            logger.info("Patch loaded successfully.");

        } catch (IOException e) {
            logger.error("Error initializing PdService", e);
        }
    }

    @PreDestroy
    public void cleanup() {
        closePatch();
    }

    public void startAudio() {
        PdBase.computeAudio(true);
    }

    public void stopAudio() {
        PdBase.computeAudio(false);
    }

    public void closePatch() {
        if (patchHandle != 0) {
            PdBase.closePatch(patchHandle);
        }
    }

    public void sendMessageToPatch(String receiverName, float value) {
        PdBase.sendFloat(receiverName, value);
    }

    public void setFrequency(float frequencyValue) {
        PdBase.sendFloat("freq_slider", frequencyValue);
    }
}