package com.cineline.lanka.controller;

import com.cineline.lanka.dto.AdminRegistrationDto;
import com.cineline.lanka.model.admin.Admin;
import com.cineline.lanka.repository.AdminRepository;
import com.cineline.lanka.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin/management")
public class AdminManagementController {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private AdminService adminService;

    // GET /admin/management/dashboard - Displays the staff list page
    @GetMapping("/dashboard")
    public String viewAdminManagement(Model model) {
        List<Admin> allAdmins = adminRepository.findAll();
        model.addAttribute("admins", allAdmins);
        return "admin/management/admin_management";
    }

    // POST /admin/management/update-role - Handles role change form submission
    @PostMapping("/update-role")
    public String updateAdminRole(@RequestParam Long adminId, @RequestParam String newRole) {
        try {
            adminService.updateRole(adminId, newRole);
            return "redirect:/admin/management/dashboard?success=roleUpdated";
        } catch (RuntimeException e) {
            // Log the error and redirect with an error status if necessary
            return "redirect:/admin/management/dashboard?error=Role update failed.";
        }
    }

    // POST /admin/management/register-new-staff - Handles the new staff registration form
    @PostMapping("/register-new-staff")
    public String registerNewStaff(AdminRegistrationDto registrationDto, Model model) {
        try {
            // The AdminService handles password hashing and saving
            adminService.registerNewAdmin(registrationDto);
            return "redirect:/admin/management/dashboard?success=staffAdded";

        } catch (RuntimeException e) {
            // If username is taken, display the error on the current page
            model.addAttribute("error", e.getMessage());

            // We must re-fetch the list of admins before returning the view
            List<Admin> allAdmins = adminRepository.findAll();
            model.addAttribute("admins", allAdmins);

            return "admin/management/admin_management"; // Return the page to show the error
        }
    }
}