package com.cineline.lanka.model.theater;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "theater")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Theater {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theater_id")
    private Long theaterId; // Primary Key: TheaterID

    @Column(name = "name", nullable = false, length = 50)
    private String name; // Theater Name (e.g., "Screen 1", "Gold Class")

    @Column(name = "capacity", nullable = false)
    private Integer capacity; // Total number of seats

    @Column(name = "location_city", length = 50)
    private String locationCity; // The city/location of the cinema (e.g., "Colombo", "Kandy")

    // Optional: Could be "Standard", "VIP", "IMAX", etc.
    @Column(name = "screen_type", length = 20)
    private String screenType;
}