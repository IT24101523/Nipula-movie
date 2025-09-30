package com.cineline.lanka.repository;

import com.cineline.lanka.model.security.Admin; // Use the correct entity path and name
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
// NOTE: Must use the 'Admin' entity name here
public interface AdminRepository extends JpaRepository<Admin, Long> {

    // This is the method we need for the login process
    Optional<Admin> findByUsername(String username);
}