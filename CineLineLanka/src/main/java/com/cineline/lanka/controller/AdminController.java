package com.cineline.lanka.controller;

import com.cineline.lanka.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    // Inject the service needed for dashboard statistics
    private final CustomerService customerService;

    // NOTE: You would inject other services here (e.g., MovieService, BookingService)
    // to fetch more dashboard data.

    @Autowired
    public AdminController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Handles the main Admin Dashboard view and loads statistics.
     * Mapped to: GET /admin/dashboard
     */
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {

        // --- Dashboard Statistics ---

        // 1. Get total customer count from CustomerService
        // This makes the variable 'totalCustomers' available in admin_dashboard.html
        long totalCustomers = customerService.countAllCustomers();
        model.addAttribute("totalCustomers", totalCustomers);

        // 2. Add other statistics here as you implement them
        // Example: model.addAttribute("totalMovies", movieService.countAllMovies());

        return "admin/admin_dashboard";
    }
}