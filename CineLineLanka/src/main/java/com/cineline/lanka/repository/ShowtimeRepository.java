package com.cineline.lanka.repository;

import com.cineline.lanka.model.showtime.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {

    // IMPORTANT: Custom query to check for overlapping showtimes in the same theater.
    @Query("SELECT s FROM Showtime s WHERE s.theater.theaterId = :theaterId AND s.showtimeId <> :showtimeId AND " +
            "((s.startTime < :endTime AND s.endTime > :startTime))")
    List<Showtime> findConflictingShowtimes(@Param("theaterId") Long theaterId,
                                            @Param("showtimeId") Long showtimeId,
                                            @Param("startTime") LocalDateTime startTime,
                                            @Param("endTime") LocalDateTime endTime);

    // Read methods for the public site/API (e.g., find all showtimes for a movie or theater)
    List<Showtime> findByMovie_MovieId(Long movieId);
    List<Showtime> findByTheater_TheaterId(Long theaterId);
}