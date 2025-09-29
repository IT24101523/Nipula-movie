package com.cineline.lanka.model.showtime;

import com.cineline.lanka.model.movie.Movie;
import com.cineline.lanka.model.theater.Theater;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "showtime")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Showtime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "showtime_id")
    private Long showtimeId;

    // The Movie playing in this showtime
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    // The Theater hosting this showtime
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id", nullable = false)
    private Theater theater;

    // The start time of the show
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    // The end time is calculated based on movie duration
    @Column(name = "end_time")
    private LocalDateTime endTime;

    // Price for one ticket
    @Column(name = "ticket_price", nullable = false)
    private Double ticketPrice;
}