package com.desiato.puresynth.services;

import com.desiato.puresynth.models.PdReceiverAdapter;
import org.puredata.core.PdBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class PdService {

    @Autowired
    private PdReceiverAdapter pdReceiverAdapter;

    private int patchHandle;

    public PdService() {
        try {
            // Initialize libpd.
            PdBase.openAudio(1, 2, 44100);

            // Set a receiver to handle messages from Pd.
            PdBase.setReceiver(new PdReceiverAdapter());

            // Load a patch.
            File patchFile = new File("");
            patchHandle = PdBase.openPatch(patchFile.getAbsolutePath());
        } catch (IOException e) {
            // Handle exceptions.
            e.printStackTrace();
        }
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
}
