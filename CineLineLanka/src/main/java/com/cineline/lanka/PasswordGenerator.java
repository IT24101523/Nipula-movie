package com.cineline.lanka; // <-- Use the appropriate package

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "fdpassword"; // <-- The simple password you will use to log in
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("BCrypt Hash to use in SQL: " + encodedPassword);
    }
}