package com.cineline.lanka.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GeneralController {

    /**
     * Handles the root path ("/") and redirects to the login page.
     */
    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    // NOTE: The conflicting /login mapping MUST be in LoginController.java

    /**
     * Maps the /403 URL to the custom 403.html template
     * when Spring Security throws an AccessDeniedException.
     * Looks for: src/main/resources/templates/403.html
     */
    @GetMapping("/403")
    public String accessDenied() {
        return "403";
    }
}