package com.cineline.lanka.controller;

import com.cineline.lanka.dto.CustomerFormDto;
import com.cineline.lanka.model.customer.Customer;
import com.cineline.lanka.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/users")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // ===============================================
    // R - READ (List All Customers/Users) - Existing from earlier step
    // ===============================================
    @GetMapping("/list")
    public String listCustomers(Model model) {
        List<Customer> users = customerService.getAllCustomers();
        model.addAttribute("users", users);
        return "admin/customer/list_users";
    }

    // ===============================================
    // C - CREATE (Show Form)
    // ===============================================
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("customer", new CustomerFormDto());
        model.addAttribute("isEdit", false);
        return "admin/customer/customer_form";
    }

    // ===============================================
    // U - UPDATE (Show Edit Form)
    // ===============================================
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Customer customer = customerService.getCustomerById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        CustomerFormDto dto = customerService.convertToFormDto(customer);

        model.addAttribute("customer", dto);
        model.addAttribute("isEdit", true);
        return "admin/customer/customer_form";
    }

    // ===============================================
    // C/U - SAVE (Handle Form Submission)
    // ===============================================
    @PostMapping("/save")
    public String saveCustomer(@Valid @ModelAttribute("customer") CustomerFormDto customerDto,
                               BindingResult result,
                               Model model) {

        if (result.hasErrors()) {
            model.addAttribute("isEdit", customerDto.getCustomerId() != null);
            return "admin/customer/customer_form";
        }

        // Additional business rule validation (e.g., check for unique email on update)
        // This is simplified, full email validation would need a custom query check.

        customerService.saveCustomer(customerDto);
        return "redirect:/admin/users/list?success";
    }

    // ===============================================
    // D - DELETE
    // ===============================================
    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable("id") Long id) {
        customerService.deleteCustomer(id);
        return "redirect:/admin/users/list?deleteSuccess";
    }
}