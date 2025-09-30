package com.cineline.lanka.repository;

import com.cineline.lanka.model.admin.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    // Spring Data JPA automatically implements this method to find the user by username
    Optional<Admin> findByUsername(String username);
}