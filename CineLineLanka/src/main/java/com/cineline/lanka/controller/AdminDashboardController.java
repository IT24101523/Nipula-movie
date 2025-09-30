package com.cineline.lanka.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    /**
     * Maps the secured URL /admin/dashboard to the correct template.
     * Looks for: src/main/resources/templates/admin/admin_dashboard.html
     */
    @GetMapping("/dashboard")
    public String adminDashboard() {
        // Return value must be the folder name + file name (without .html)
        return "admin/admin_dashboard";
    }
}