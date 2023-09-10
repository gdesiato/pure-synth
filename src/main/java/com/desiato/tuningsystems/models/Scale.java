package com.desiato.tuningsystems.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Scale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 1000) // adjust length as necessary
    private String description;

    @ManyToMany
    @JoinTable(
            name = "scale_note",
            joinColumns = @JoinColumn(name = "scale_id"),
            inverseJoinColumns = @JoinColumn(name = "note_id")
    )
    private List<Note> notes;

    @ManyToOne
    @JoinColumn(name = "tuning_system_id", nullable = false)
    private TuningSystem tuningSystem;

}
