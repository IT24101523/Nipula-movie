package com.cineline.lanka.model.movie;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "movie")
@Data // Lombok: Generates getters, setters, toString, equals, and hashCode
@NoArgsConstructor // Lombok: Generates no-argument constructor
@AllArgsConstructor // Lombok: Generates constructor with all fields
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id")
    private Long movieId; // Primary Key: MovieID

    @Column(name = "title", nullable = false, length = 100)
    private String title; // Title

    @Column(name = "genre", length = 50)
    private String genre; // Genre

    @Column(name = "duration", nullable = false)
    private Integer duration; // Duration in minutes (e.g., 120)

    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate; // ReleaseDate
}