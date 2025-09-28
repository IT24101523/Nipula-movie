package com.cineline.lanka.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GeneralController {

    // Maps to the custom login page defined in SecurityConfig
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Landing page for Admin users after successful login
    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "admin/admin_dashboard";
    }
}