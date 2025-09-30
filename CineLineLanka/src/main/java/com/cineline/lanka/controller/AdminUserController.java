package com.cineline.lanka.controller;

import com.cineline.lanka.dto.AdminFormDto;
import com.cineline.lanka.model.security.Admin; // Adjust path to your security Admin entity
import com.cineline.lanka.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/admins")
public class AdminUserController {

    private final AdminService adminService;

    // Define allowed roles for the select dropdown in the form
    private final List<String> availableRoles = List.of(
            "ROLE_SUPER_ADMIN",
            "ROLE_MANAGER",
            "ROLE_FRONTDESK_EXECUTIVE"
    );

    @Autowired
    public AdminUserController(AdminService adminService) {
        this.adminService = adminService;
    }

    // ===============================================
    // R - READ (List All Admins/Staff)
    // ===============================================
    @GetMapping("/list")
    public String listAdmins(Model model) {
        List<Admin> admins = adminService.getAllAdmins();
        model.addAttribute("admins", admins);
        return "admin/admin/list_admins";
    }

    // ===============================================
    // C - CREATE (Show Form)
    // ===============================================
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("adminUser", new AdminFormDto());
        model.addAttribute("isEdit", false);
        model.addAttribute("roles", availableRoles);
        return "admin/admin/admin_form";
    }

    // ===============================================
    // U - UPDATE (Show Edit Form)
    // ===============================================
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Admin admin = adminService.getAdminById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Admin Id:" + id));

        // Note: We don't map the password back to the DTO for security.
        AdminFormDto dto = new AdminFormDto();
        dto.setId(admin.getId());
        dto.setUsername(admin.getUsername());
        dto.setEmail(admin.getEmail());
        dto.setRole(admin.getRole());

        model.addAttribute("adminUser", dto);
        model.addAttribute("isEdit", true);
        model.addAttribute("roles", availableRoles);
        return "admin/admin/admin_form";
    }

    // ===============================================
    // C/U - SAVE (Handle Form Submission)
    // ===============================================
    @PostMapping("/save")
    public String saveAdmin(@Valid @ModelAttribute("adminUser") AdminFormDto adminDto,
                            BindingResult result,
                            Model model) {

        // Custom validation: Passwords must match on create or if a new one is provided
        if (adminDto.getPassword() != null && !adminDto.getPassword().isEmpty()) {
            if (!adminDto.getPassword().equals(adminDto.getConfirmPassword())) {
                result.rejectValue("confirmPassword", "password.mismatch", "Passwords must match.");
            }
        } else if (adminDto.getId() == null) {
            // Password is mandatory for new user creation
            if (adminDto.getPassword() == null || adminDto.getPassword().isEmpty()) {
                result.rejectValue("password", "password.required", "Password is required for new accounts.");
            }
        }

        if (result.hasErrors()) {
            model.addAttribute("isEdit", adminDto.getId() != null);
            model.addAttribute("roles", availableRoles);
            return "admin/admin/admin_form";
        }

        try {
            adminService.saveAdmin(adminDto);
        } catch (IllegalArgumentException e) {
            // Catch unique constraint errors (e.g., username exists)
            result.rejectValue("username", "username.duplicate", e.getMessage());
            model.addAttribute("isEdit", adminDto.getId() != null);
            model.addAttribute("roles", availableRoles);
            return "admin/admin/admin_form";
        }

        return "redirect:/admin/admins/list?success";
    }

    // ===============================================
    // D - DELETE
    // ===============================================
    @GetMapping("/delete/{id}")
    public String deleteAdmin(@PathVariable("id") Long id) {
        // Implement logic in service to prevent Super-Admin from deleting self!
        adminService.deleteAdmin(id);
        return "redirect:/admin/admins/list?deleteSuccess";
    }
}