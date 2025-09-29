package com.cineline.lanka.service;

import com.cineline.lanka.model.showtime.Showtime;
import com.cineline.lanka.repository.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ShowtimeService {

    private final ShowtimeRepository showtimeRepository;

    @Autowired
    public ShowtimeService(ShowtimeRepository showtimeRepository) {
        this.showtimeRepository = showtimeRepository;
    }

    // ===============================================
    // FIX APPLIED HERE: Force Eager Loading
    // ===============================================
    @Transactional(readOnly = true)
    public List<Showtime> getAllShowtimes() {
        List<Showtime> showtimes = showtimeRepository.findAll();

        // This loop forces Hibernate to fetch the related Movie and Theater
        // objects for each Showtime while still inside the active transaction.
        showtimes.forEach(showtime -> {
            // Check for null to prevent crashes if a foreign key is actually missing in the DB
            if (showtime.getMovie() != null) {
                showtime.getMovie().getTitle(); // Accessing a field initializes the proxy
            }
            if (showtime.getTheater() != null) {
                showtime.getTheater().getName(); // Accessing a field initializes the proxy
            }
        });

        return showtimes;
    }
    // ===============================================

    // You should also ensure your other methods are correctly implemented
    public Optional<Showtime> getShowtimeById(Long id) {
        return showtimeRepository.findById(id);
    }

    @Transactional
    public Showtime createShowtime(Showtime showtime) {
        // Add business logic/validation if needed
        return showtimeRepository.save(showtime);
    }

    @Transactional
    public Showtime updateShowtime(Long id, Showtime showtimeDetails) {
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid showtime Id:" + id));

        // Update fields here (ensure you fetch Movie/Theater from DB if necessary,
        // but for simple update, relying on the ID in showtimeDetails might be fine)
        showtime.setMovie(showtimeDetails.getMovie());
        showtime.setTheater(showtimeDetails.getTheater());
        showtime.setStartTime(showtimeDetails.getStartTime());
        showtime.setTicketPrice(showtimeDetails.getTicketPrice());

        return showtimeRepository.save(showtime);
    }

    public void deleteShowtime(Long id) {
        if (!showtimeRepository.existsById(id)) {
            throw new IllegalArgumentException("Invalid showtime Id:" + id);
        }
        showtimeRepository.deleteById(id);
    }
}