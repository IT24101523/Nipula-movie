package com.cineline.lanka.repository;

import com.cineline.lanka.model.theater.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TheaterRepository extends JpaRepository<Theater, Long> {

    // Custom method to check for duplicate theater name in a specific city
    Optional<Theater> findByNameIgnoreCaseAndLocationCityIgnoreCase(String name, String locationCity);
}