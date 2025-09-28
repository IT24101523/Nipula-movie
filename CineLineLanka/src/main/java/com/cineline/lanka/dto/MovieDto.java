package com.cineline.lanka.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor; // <-- ADD THIS
import lombok.Data;
import lombok.NoArgsConstructor; // <-- ADD THIS
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

// DTO for Movie creation/update form data
@Data
@NoArgsConstructor // Lombok: Generates constructor with no arguments
@AllArgsConstructor // Lombok: Generates constructor with all fields
public class MovieDto {

    private Long movieId; // Used for update operations
    // ... rest of the fields ...
    // (Ensure the fields match the constructor call)

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    private String title;

    // ... (rest of the fields as before)
    @Size(max = 50, message = "Genre cannot exceed 50 characters")
    private String genre;

    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be positive")
    private Integer duration; // Duration in minutes

    @NotNull(message = "Release Date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd") // Standard HTML date format
    private LocalDate releaseDate;
}