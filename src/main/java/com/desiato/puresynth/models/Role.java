package com.desiato.puresynth.models;

import jakarta.persistence.*;
import lombok.Data;


@Table(name = "roles")
@Data
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }
}