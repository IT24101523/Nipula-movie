package com.cineline.lanka.service;

import com.cineline.lanka.model.movie.Movie; // Make sure this is imported
import com.cineline.lanka.repository.MovieRepository; // <-- This is the crucial line!
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    // Dependency Injection via Constructor
    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    // ===============================================
    // C - CREATE Operation
    // ===============================================

    public Movie createMovie(Movie movie) {
        // Business Rule: Check for duplicate movie title
        if (movieRepository.findByTitleIgnoreCase(movie.getTitle()).isPresent()) {
            throw new IllegalArgumentException("A movie with the title '" + movie.getTitle() + "' already exists.");
        }

        // Business Rule: Movies must have valid release dates
        // Allow release dates up to 1 year in the future, but validation logic is here.
        if (movie.getReleaseDate() == null) {
            throw new IllegalArgumentException("Release date cannot be empty.");
        }

        // Save the new movie
        return movieRepository.save(movie);
    }

    // ===============================================
    // R - READ Operations
    // ===============================================

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Optional<Movie> getMovieById(Long movieId) {
        return movieRepository.findById(movieId);
    }

    // ===============================================
    // U - UPDATE Operation
    // ===============================================

    public Movie updateMovie(Long movieId, Movie movieDetails) {
        return movieRepository.findById(movieId).map(movie -> {
            // Check for duplicate title if the title is being changed
            if (!movie.getTitle().equalsIgnoreCase(movieDetails.getTitle()) &&
                    movieRepository.findByTitleIgnoreCase(movieDetails.getTitle()).isPresent()) {
                throw new IllegalArgumentException("A movie with the title '" + movieDetails.getTitle() + "' already exists.");
            }

            // Apply updates
            movie.setTitle(movieDetails.getTitle());
            movie.setGenre(movieDetails.getGenre());
            movie.setDuration(movieDetails.getDuration());
            movie.setReleaseDate(movieDetails.getReleaseDate());

            // Save the updated entity
            return movieRepository.save(movie);
        }).orElseThrow(() -> new IllegalArgumentException("Movie not found with ID: " + movieId));
    }

    // ===============================================
    // D - DELETE Operation
    // ===============================================

    public void deleteMovie(Long movieId) {
        if (!movieRepository.existsById(movieId)) {
            throw new IllegalArgumentException("Movie not found with ID: " + movieId);
        }
        movieRepository.deleteById(movieId);
    }
}