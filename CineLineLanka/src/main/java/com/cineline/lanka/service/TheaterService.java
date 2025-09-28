package com.cineline.lanka.service;

import com.cineline.lanka.model.theater.Theater;
import com.cineline.lanka.repository.TheaterRepository; // <--- ADD OR CORRECT THIS LINE!
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TheaterService {

    private final TheaterRepository theaterRepository;

    @Autowired
    public TheaterService(TheaterRepository theaterRepository) {
        this.theaterRepository = theaterRepository;
    }

    // C - CREATE Operation
    public Theater createTheater(Theater theater) {
        // Business Rule: Check for duplicate theater name within the same city/location
        if (theaterRepository.findByNameIgnoreCaseAndLocationCityIgnoreCase(
                theater.getName(), theater.getLocationCity()).isPresent()) {
            throw new IllegalArgumentException(
                    "A theater named '" + theater.getName() +
                            "' already exists in " + theater.getLocationCity()
            );
        }

        if (theater.getCapacity() <= 0) {
            throw new IllegalArgumentException("Theater capacity must be greater than zero.");
        }

        return theaterRepository.save(theater);
    }

    // R - READ Operations
    public List<Theater> getAllTheaters() {
        return theaterRepository.findAll();
    }

    public Optional<Theater> getTheaterById(Long theaterId) {
        return theaterRepository.findById(theaterId);
    }

    // U - UPDATE Operation
    public Theater updateTheater(Long theaterId, Theater theaterDetails) {
        return theaterRepository.findById(theaterId).map(theater -> {

            // Check for duplicate name for a different theater ID
            Optional<Theater> existingTheater = theaterRepository.findByNameIgnoreCaseAndLocationCityIgnoreCase(
                    theaterDetails.getName(), theaterDetails.getLocationCity());

            if (existingTheater.isPresent() && !existingTheater.get().getTheaterId().equals(theaterId)) {
                throw new IllegalArgumentException(
                        "Another theater named '" + theaterDetails.getName() +
                                "' already exists in " + theaterDetails.getLocationCity()
                );
            }

            // Apply updates
            theater.setName(theaterDetails.getName());
            theater.setCapacity(theaterDetails.getCapacity());
            theater.setLocationCity(theaterDetails.getLocationCity());
            theater.setScreenType(theaterDetails.getScreenType());

            return theaterRepository.save(theater);
        }).orElseThrow(() -> new IllegalArgumentException("Theater not found with ID: " + theaterId));
    }

    // D - DELETE Operation
    public void deleteTheater(Long theaterId) {
        if (!theaterRepository.existsById(theaterId)) {
            throw new IllegalArgumentException("Theater not found with ID: " + theaterId);
        }
        // NOTE: In a real system, you must handle dependent Showtimes before deletion
        theaterRepository.deleteById(theaterId);
    }
}