package com.desiato.tuningsystems.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class TuningSystem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 1000)
    private String description;


}
