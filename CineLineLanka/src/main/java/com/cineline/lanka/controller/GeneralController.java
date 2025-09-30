package com.cineline.lanka.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GeneralController {

    /**
     * Handles the public home page of the cinema site.
     * Mapped to: GET /
     */
    @GetMapping("/")
    public String home() {
        // Assume you have an index.html or public/home.html template
        return "index";
    }

    /**
     * Handles the custom login page.
     * Mapped to: GET /login
     */
    @GetMapping("/login")
    public String login() {
        return "login"; // Renders src/main/resources/templates/login.html
    }

    /**
     * Handles the customer registration page.
     * Mapped to: GET /register
     */
    @GetMapping("/register")
    public String register() {
        // You'll likely need a form DTO and registration logic later
        return "register"; // Renders src/main/resources/templates/register.html
    }

    // NOTE: The old @GetMapping("/admin/dashboard") method has been removed
    // to resolve the Ambiguous Mapping error.
}