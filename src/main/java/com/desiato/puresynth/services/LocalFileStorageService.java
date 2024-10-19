package com.desiato.puresynth.services;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class LocalFileStorageService implements FileStorageService {

    private static final String BASE_PATH = "audio_files/";

    @Override
    public String saveFile(byte[] data, String fileName) throws IOException {

        Path filePath = Paths.get(BASE_PATH + fileName + ".wav");

        // .createDirectories() checks if a directory exists, or creates one
        Files.createDirectories(filePath.getParent());

        Files.write(filePath, data);

        return filePath.toString();
    }
}
