package com.desiato.puresynth.services;

import org.springframework.stereotype.Service;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class AudioService {

    private static final int SAMPLE_RATE = 44100;
    private static final int BITS_DEPTH = 16;
    private static final boolean BIG_ENDIAN = false;

    public byte[] generateSineWaveFile(double frequency, double durationInSeconds) throws IOException, LineUnavailableException {
        // Generate the sine wave data as a byte array
        byte[] buffer = generateSineWaveBuffer(frequency, durationInSeconds);

        // Create an audio format
        AudioFormat format = new AudioFormat(SAMPLE_RATE, BITS_DEPTH, 1, true, BIG_ENDIAN);

        // Use ByteArrayOutputStream to avoid saving to disk
        try (ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
             AudioInputStream ais = new AudioInputStream(bais, format, buffer.length / 2)) {
            AudioSystem.write(ais, AudioFileFormat.Type.WAVE, baos);
            return baos.toByteArray();
        }
    }

    private byte[] generateSineWaveBuffer(double frequency, double durationInSeconds) {
        int bufferLength = (int) (durationInSeconds * SAMPLE_RATE);

        // Buffer is initialized to store the waveform data
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
