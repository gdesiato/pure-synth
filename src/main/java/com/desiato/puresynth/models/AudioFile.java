package com.desiato.puresynth.models;

import javax.sound.sampled.AudioFormat;

public record AudioFile(byte[] data, String fileName, AudioFormat format) {

}
