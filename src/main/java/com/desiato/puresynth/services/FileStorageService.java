package com.desiato.puresynth.services;

import java.io.IOException;

public interface FileStorageService {
    String saveFile(byte[] data, String fileName) throws IOException;
}
