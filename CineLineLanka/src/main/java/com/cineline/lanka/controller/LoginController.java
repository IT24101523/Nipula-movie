package com.cineline.lanka.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    /**
     * Maps the /login URL to the login.html template.
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}