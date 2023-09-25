package com.desiato.puresynth.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tuning_system_id", nullable = false)
    private TuningSystem tuningSystem;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double frequency;

    @Column
    private String audioFilePath;

}
