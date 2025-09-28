package com.cineline.lanka.repository;

import com.cineline.lanka.model.movie.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
// JpaRepository<Entity, PrimaryKeyDataType>
public interface MovieRepository extends JpaRepository<Movie, Long> {

    // Custom method to check for duplicate movie title
    Optional<Movie> findByTitleIgnoreCase(String title);
}