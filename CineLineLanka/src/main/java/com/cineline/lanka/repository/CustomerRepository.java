package com.cineline.lanka.repository;

import com.cineline.lanka.model.customer.Customer; // <-- Imports the Customer Entity
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // This interface now provides the necessary methods (count, save, findById, etc.)
    // that CustomerService is trying to call.

    // Optional: Add findByEmail for unique check, though not strictly required for compilation
    // Optional<Customer> findByEmail(String email);
}