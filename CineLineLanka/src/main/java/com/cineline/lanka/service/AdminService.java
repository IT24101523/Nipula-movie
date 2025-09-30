package com.cineline.lanka.service;

import com.cineline.lanka.model.admin.Admin;
import com.cineline.lanka.repository.AdminRepository;
import com.cineline.lanka.dto.AdminRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ----------------------------------------------------------------------
    // 1. Method for SUPER_ADMIN to add new staff
    // ----------------------------------------------------------------------
    public Admin registerNewAdmin(AdminRegistrationDto registrationDto) {
        if (adminRepository.findByUsername(registrationDto.getUsername()).isPresent()) {
            throw new RuntimeException("Username is already taken: " + registrationDto.getUsername());
        }

        Admin admin = new Admin();
        admin.setUsername(registrationDto.getUsername());
        admin.setEmail(registrationDto.getEmail());

        // CRITICAL: Encode password before saving
        admin.setPassword(passwordEncoder.encode(registrationDto.getPassword()));

        // Use the role provided, or a default staff role
        String role = registrationDto.getRole() != null && !registrationDto.getRole().isEmpty()
                ? registrationDto.getRole().toUpperCase().trim()
                : "FRONTDESK_EXECUTIVE";

        // Note: We skip complex validation here, relying on the client side
        // and the updateRole validation to guide valid roles.

        admin.setRole(role);
        admin.setEnabled(true);
        return adminRepository.save(admin);
    }

    // ----------------------------------------------------------------------
    // 2. Method for SUPER_ADMIN to update staff roles
    // ----------------------------------------------------------------------
    public Admin updateRole(Long adminId, String newRole) {
        Optional<Admin> adminOptional = adminRepository.findById(adminId);

        if (adminOptional.isEmpty()) {
            throw new RuntimeException("Admin not found with ID: " + adminId);
        }

        Admin admin = adminOptional.get();
        String sanitizedRole = newRole.toUpperCase().trim();

        // UPDATED: All allowed roles are validated here
        if (!sanitizedRole.equals("SUPER_ADMIN") &&
                !sanitizedRole.equals("FRONTDESK_EXECUTIVE") &&
                !sanitizedRole.equals("MANAGER") &&
                !sanitizedRole.equals("MARKETING_COORDINATOR") &&
                !sanitizedRole.equals("IT_SUPPORT_OFFICER"))
        {
            throw new IllegalArgumentException("Invalid role specified. Must be one of: SUPER_ADMIN, MANAGER, FRONTDESK_EXECUTIVE, MARKETING_COORDINATOR, or IT_SUPPORT_OFFICER.");
        }

        admin.setRole(sanitizedRole);
        return adminRepository.save(admin);
    }
}