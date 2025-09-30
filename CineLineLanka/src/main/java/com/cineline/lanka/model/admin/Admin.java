package com.cineline.lanka.model.admin;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data // Provides all getters, setters, constructors, etc.
@Table(name = "admin") // Ensure this matches your actual table name
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password; // The encoded password

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String role; // Stores the role, e.g., "ROLE_SUPER_ADMIN"

    @Column(nullable = false)
    private boolean enabled; // Maps to 'enabled' column (1 or 0)
}