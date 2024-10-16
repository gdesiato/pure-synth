package com.desiato.puresynth.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
@Service
public class AudioService {

    private static final int SAMPLE_RATE = 44100;
    private static final int BITS_DEPTH = 16;
    private static final boolean BIG_ENDIAN = false;

    public byte[] generateSineWaveFile(double frequency, double durationInSeconds) throws IOException {
        // Generate the sine wave data as a byte array
        byte[] buffer = generateSineWaveBuffer(frequency, durationInSeconds);

        log.info("Buffer length (bytes): " + buffer.length);

        AudioFormat format = new AudioFormat(SAMPLE_RATE, BITS_DEPTH, 1, true, BIG_ENDIAN);

        // Use ByteArrayOutputStream to avoid saving to disk
        try (ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
             AudioInputStream ais = new AudioInputStream(bais, format, buffer.length / 2)) {

            // Write the audio data to a byte array
            AudioSystem.write(ais, AudioFileFormat.Type.WAVE, baos);
            return baos.toByteArray();
        }
    }

    private byte[] generateSineWaveBuffer(double frequency, double durationInSeconds) {
        int bufferLength = (int) (durationInSeconds * SAMPLE_RATE);
        byte[] buffer = new byte[bufferLength * 2];

        for (int i = 0; i < bufferLength; i++) {
            // Generate the sine wave sample
            double angle = 2.0 * Math.PI * i * frequency / SAMPLE_RATE;
            short sampleValue = (short) (Math.sin(angle) * Short.MAX_VALUE);

            // Convert the sample value to bytes (little-endian format)
            buffer[2 * i] = (byte) (sampleValue & 0xFF);          // Lower byte
            buffer[2 * i + 1] = (byte) ((sampleValue >> 8) & 0xFF); // Upper byte
        }

        return buffer;
    }
}
