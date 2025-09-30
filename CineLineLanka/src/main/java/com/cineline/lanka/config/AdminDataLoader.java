package com.cineline.lanka.config;

// Use the confirmed package path for your Admin entity
import com.cineline.lanka.model.security.Admin;
// Use the confirmed package path for your repository
import com.cineline.lanka.repository.AdminRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminDataLoader implements CommandLineRunner {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Only create the user if one doesn't exist
        if (adminRepository.findByUsername("superadmin").isEmpty()) {
            Admin admin = new Admin();
            admin.setUsername("superadmin");
            admin.setEmail("admin@cineline.com");
            // **THIS IS THE CRUCIAL LINE: The password is encoded!**
            admin.setPassword(passwordEncoder.encode("mypass123"));
            admin.setRole("SUPER_ADMIN");

            adminRepository.save(admin);
            System.out.println("--- Default Super Admin 'superadmin' created with password 'mypass123' ---");
        }
    }
}