package com.desiato.puresynth.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

@Service
public class AudioService {

    private static final int SAMPLE_RATE = 44100;
    private static final int BITS_DEPTH = 16;
    private static final boolean BIG_ENDIAN = false;

    @Value("${audio.files.directory}")
    private String audioFilesDir;

    public File generateSineWaveFile(double frequency, double durationInSeconds) throws IOException, LineUnavailableException {
        // Generate the sine wave data. It creates an array of bytes
        byte[] buffer = generateSineWaveBuffer(frequency, durationInSeconds);

        // Create an audio file
        File audioFile = new File(audioFilesDir, "sine_wave_" + frequency + "Hz.wav");
        AudioFormat format = new AudioFormat(SAMPLE_RATE, BITS_DEPTH, 1, true, BIG_ENDIAN);
        try (AudioInputStream ais = new AudioInputStream(new ByteArrayInputStream(buffer), format, buffer.length)) {
            AudioSystem.write(ais, AudioFileFormat.Type.WAVE, audioFile);
        }

        return audioFile;
    }


    private byte[] generateSineWaveBuffer(double frequency, double durationInSeconds) {

        int bufferLength = (int) (durationInSeconds * SAMPLE_RATE);

        // buffer is initialized to store the waveform data
        byte[] buffer = new byte[bufferLength * 2]; // 2 bytes per frame for 16-bit samples

        for (int i = 0; i < bufferLength; i++) {
            double angle = 2.0 * Math.PI * i * frequency / SAMPLE_RATE;
            short sampleValue = (short) (Math.sin(angle) * Short.MAX_VALUE);

            buffer[2 * i] = (byte) (sampleValue & 0xFF);
            buffer[2 * i + 1] = (byte) ((sampleValue >> 8) & 0xFF);
        }

        return buffer;
    }
}
