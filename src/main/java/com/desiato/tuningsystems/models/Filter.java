package com.desiato.tuningsystems.models;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class Filter {

    private String filterType; // Low-pass, High-pass, Band-pass, etc.
    private double cutoffFrequency;
    private double resonance;


}
