package com.desiato.puresynth.dtos;

import javax.sound.sampled.AudioFormat;

public record AudioFileDTO(Long fileId, byte[] data, String fileName, AudioFormat format) {

}
