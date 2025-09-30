package com.cineline.lanka.service;

import com.cineline.lanka.dto.AdminFormDto;
import com.cineline.lanka.model.security.Admin; // Adjust path
import com.cineline.lanka.repository.AdminRepository; // Adjust path
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Admin> getAllAdmins() {
        // Exclude the currently logged-in Super-Admin from the list for safety,
        // though JpaRepository.findAll() is standard.
        return adminRepository.findAll();
    }

    public Optional<Admin> getAdminById(Long id) {
        return adminRepository.findById(id);
    }

    @Transactional
    public Admin saveAdmin(AdminFormDto dto) {
        // Basic check to ensure passwords match if a new password is provided
        if (dto.getPassword() != null && !dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match.");
        }

        Admin admin;

        if (dto.getId() != null) {
            // UPDATE
            admin = adminRepository.findById(dto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Admin ID: " + dto.getId()));

        } else {
            // CREATE
            // Check for unique username/email before creating
            if (adminRepository.findByUsername(dto.getUsername()).isPresent()) {
                throw new IllegalArgumentException("Username already exists.");
            }
            admin = new Admin();
            // Default active status
            // admin.setEnabled(true);
        }

        // Map fields
        admin.setUsername(dto.getUsername());
        admin.setEmail(dto.getEmail());
        admin.setRole(dto.getRole()); // Set the role from DTO

        // Handle password change/setting (only hash if a new password is provided)
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            admin.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        return adminRepository.save(admin);
    }

    public void deleteAdmin(Long id) {
        // Implement logic to prevent Super-Admin from deleting themselves!
        if (!adminRepository.existsById(id)) {
            throw new IllegalArgumentException("Invalid Admin ID: " + id);
        }
        adminRepository.deleteById(id);
    }
}