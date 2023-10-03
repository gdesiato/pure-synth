package com.desiato.puresynth.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Oscillator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String waveformType; // Sine, Square, Sawtooth, Triangle, etc.
    private double frequency;
    private double amplitude;

}
