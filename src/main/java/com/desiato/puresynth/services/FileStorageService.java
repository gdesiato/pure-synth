package com.desiato.puresynth.services;

import com.desiato.puresynth.models.AudioFile;

import java.io.IOException;

public interface FileStorageService {
    String saveFile(byte[] data, String fileName) throws IOException;
    AudioFile retrieveFile(String fileName) throws IOException;
}
