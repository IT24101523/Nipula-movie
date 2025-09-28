package com.cineline.lanka.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO for Theater creation/update form data
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TheaterDto {

    private Long theaterId; // Used for update operations

    @NotBlank(message = "Theater name is required")
    @Size(max = 50, message = "Name cannot exceed 50 characters")
    private String name;

    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    private Integer capacity;

    @NotBlank(message = "Location City is required")
    @Size(max = 50, message = "Location cannot exceed 50 characters")
    private String locationCity;

    @Size(max = 20, message = "Screen Type cannot exceed 20 characters")
    private String screenType;
}