package com.desiato.puresynth.models;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class ADSR {

    private double attackTime;
    private double decayTime;
    private double sustainLevel;
    private double releaseTime;

}