package com.cineline.lanka.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShowtimeDto {

    private Long showtimeId; // Used for update operations

    @NotNull(message = "Movie is required")
    private Long movieId;

    @NotNull(message = "Theater is required")
    private Long theaterId;

    @NotNull(message = "Start time is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") // Standard HTML datetime-local format
    @FutureOrPresent(message = "Start time must be in the present or future")
    private LocalDateTime startTime;

    // End Time is calculated in the service, so it's not needed for the form

    @NotNull(message = "Ticket price is required")
    @Min(value = 1, message = "Ticket price must be greater than zero")
    private Double ticketPrice;
}