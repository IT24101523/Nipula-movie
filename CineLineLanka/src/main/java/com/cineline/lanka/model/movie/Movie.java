package com.cineline.lanka.model.movie;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "movie")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id")
    private Long movieId;

    private String title;

    private String genre;

    private Integer duration; // Duration in minutes

    @Column(name = "release_date")
    private LocalDate releaseDate;

    // --- CONSTRUCTORS ---

    // 1. No-Argument Constructor (MANDATORY for JPA/Hibernate)
    public Movie() {}

    // 2. Standard 4-Argument Constructor (Without ID)
    public Movie(String title, String genre, Integer duration, LocalDate releaseDate) {
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.releaseDate = releaseDate;
    }

    // 3. CRITICAL FIX: The 5-Argument Constructor your MovieController requires
    public Movie(Long movieId, String title, String genre, Integer duration, LocalDate releaseDate) {
        this.movieId = movieId;
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.releaseDate = releaseDate;
    }

    // --- GETTERS AND SETTERS ---

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }
}