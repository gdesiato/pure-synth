package com.desiato.puresynth.services;

import com.desiato.puresynth.models.AudioFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
public class LocalFileStorageService implements FileStorageService {

    private static final String BASE_PATH = "audio_files/";

    @Override
    public String saveFile(byte[] data, String fileName) {
        Path filePath = Paths.get(BASE_PATH + fileName + ".wav");

        try {
            Files.createDirectories(filePath.getParent());

            Files.write(filePath, data);

            return filePath.toString();

        } catch (IOException e) {
            log.error("Error saving file '{}': {}", fileName, e.getMessage());
            throw new RuntimeException("Failed to save the file due to an unexpected error.");
        }
    }

    @Override
    public AudioFile retrieveFile(String fileName) throws IOException {
        Path filePath = Paths.get(BASE_PATH + fileName + ".wav");

        try {
            if (Files.exists(filePath)) {
                byte[] fileData = Files.readAllBytes(filePath);
                AudioFormat format = new AudioFormat(
                        44100,
                        16,
                        1,
                        true,
                        false);

                return new AudioFile(fileData, fileName, format);
            } else {
                throw new IOException("File not found: " + fileName);
            }
        } catch (IOException e) {
            log.error("Error retrieving file '{}': {}", fileName, e.getMessage());
            throw new RuntimeException("Failed to retrieve the file.");
        }
    }
}
