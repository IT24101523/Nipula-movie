package com.cineline.lanka.dto;

import lombok.Data;

@Data
public class AdminRegistrationDto {
    private String username;
    private String email;
    private String password;
    private String role;
}