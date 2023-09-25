package com.desiato.puresynth.models;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class Oscillator {

    private String waveformType; // Sine, Square, Sawtooth, Triangle, etc.
    private double frequency;
    private double amplitude;

}
