package com.desiato.tuningsystems.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;  // Remember to hash and salt passwords before storing!

    // Assuming that user preferences are a set of strings (like 'Piano', 'EqualTemperament')
    // This can be refined further based on specific requirements.
    @ElementCollection
    private Set<String> userPreferences;


}
