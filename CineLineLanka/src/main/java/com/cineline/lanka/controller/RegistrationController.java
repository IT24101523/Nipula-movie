package com.cineline.lanka.controller;

import com.cineline.lanka.dto.AdminRegistrationDto;
import com.cineline.lanka.model.admin.Admin;
import com.cineline.lanka.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/management")
public class RegistrationController {

    @Autowired
    private AdminService adminService;

    /**
     * Endpoint to register a new staff member.
     * This is protected by SecurityConfig, requiring the caller to have ROLE_SUPER_ADMIN.
     * * Request Body Example (JSON):
     * {
     * "username": "newmanager",
     * "email": "manager@cinema.com",
     * "password": "temp_password",
     * "role": "MANAGER"
     * }
     */
    @PostMapping("/register-staff")
    public ResponseEntity<?> registerAdmin(@RequestBody AdminRegistrationDto registrationDto) {
        try {
            // AdminService handles password encoding and database insertion
            Admin newAdmin = adminService.registerNewAdmin(registrationDto);

            // Return safe response data (excluding the password hash)
            Admin responseAdmin = new Admin();
            responseAdmin.setUsername(newAdmin.getUsername());
            responseAdmin.setRole(newAdmin.getRole());
            responseAdmin.setEmail(newAdmin.getEmail());

            return new ResponseEntity<>(responseAdmin, HttpStatus.CREATED);

        } catch (RuntimeException e) {
            // e.g., Username is already taken
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}