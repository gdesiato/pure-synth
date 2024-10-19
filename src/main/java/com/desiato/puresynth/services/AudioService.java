package com.desiato.puresynth.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
@Service
public class AudioService {

    private final FileStorageService fileStorageService;

    @Autowired
    public AudioService(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    public byte[] generateSineWaveFile(double frequency, double durationInSeconds) throws IOException {
        byte[] buffer = generateSineWaveBuffer(frequency, durationInSeconds);

        AudioFormat format = new AudioFormat(
                44100,
                16,
                1,
                true,
                false);

        try (ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
             AudioInputStream ais = new AudioInputStream(bais, format, buffer.length / 2)) {

            AudioSystem.write(ais, AudioFileFormat.Type.WAVE, baos);
            return baos.toByteArray();
        }
    }

    public String saveAudioFile(byte[] audioBytes, String fileName) throws IOException {
        // Save the file using the FileStorageService (local or cloud storage)
        return fileStorageService.saveFile(audioBytes, fileName);
    }

    private byte[] generateSineWaveBuffer(double frequency, double durationInSeconds) {
        int bufferLength = (int) (durationInSeconds * 44100);
        byte[] buffer = new byte[bufferLength * 2]; // 16-bit audio (2 bytes per sample)

        for (int i = 0; i < bufferLength; i++) {
            double angle = 2.0 * Math.PI * i * frequency / 44100;
            short sampleValue = (short) (Math.sin(angle) * Short.MAX_VALUE);
            buffer[2 * i] = (byte) (sampleValue & 0xFF);          // Lower byte
            buffer[2 * i + 1] = (byte) ((sampleValue >> 8) & 0xFF); // Upper byte
        }

        return buffer;
    }
}
