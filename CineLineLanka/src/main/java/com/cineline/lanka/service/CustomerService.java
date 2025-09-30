package com.cineline.lanka.service;

import com.cineline.lanka.dto.CustomerFormDto;
import com.cineline.lanka.model.customer.Customer;
import com.cineline.lanka.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // R - COUNT (For Admin Dashboard)
    public long countAllCustomers() {
        return customerRepository.count();
    }

    // R - READ (For Edit/View)
    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    // R - READ (For List)
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }


    // C/U - CREATE / UPDATE (Save/Update Customer from DTO)
    @Transactional
    public Customer saveCustomer(CustomerFormDto dto) {

        Customer customer;

        if (dto.getCustomerId() != null) {
            // Update existing customer
            customer = customerRepository.findById(dto.getCustomerId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Customer ID: " + dto.getCustomerId()));

        } else {
            // Create new customer
            customer = new Customer();
            // RegistrationDate is set in the Entity constructor/default, or here:
            customer.setRegistrationDate(LocalDateTime.now());
        }

        // Map DTO fields to Entity
        customer.setName(dto.getName());
        customer.setEmail(dto.getEmail());
        customer.setPhone(dto.getPhone());

        return customerRepository.save(customer);
    }

    // D - DELETE
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new IllegalArgumentException("Invalid Customer ID: " + id);
        }
        customerRepository.deleteById(id);
    }

    // Helper to map entity to DTO for showing the edit form
    public CustomerFormDto convertToFormDto(Customer customer) {
        CustomerFormDto dto = new CustomerFormDto();
        dto.setCustomerId(customer.getCustomerId());
        dto.setName(customer.getName());
        dto.setEmail(customer.getEmail());
        dto.setPhone(customer.getPhone());
        return dto;
    }
}