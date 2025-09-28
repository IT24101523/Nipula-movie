package com.cineline.lanka.controller;

import com.cineline.lanka.dto.TheaterDto;
import com.cineline.lanka.model.theater.Theater;
import com.cineline.lanka.service.TheaterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/theaters") // Base path for all theater management URLs
public class TheaterController {

    private final TheaterService theaterService;

    @Autowired
    public TheaterController(TheaterService theaterService) {
        this.theaterService = theaterService;
    }

    // ===============================================
    // R - READ (List All Theaters)
    // URL: /admin/theaters/list
    // ===============================================
    @GetMapping("/list")
    public String listTheaters(Model model) {
        List<Theater> theaters = theaterService.getAllTheaters();
        model.addAttribute("theaters", theaters);
        return "admin/theater/list_theaters"; // Thymeleaf template location
    }

    // ===============================================
    // C - CREATE (Show Form)
    // URL: /admin/theaters/new
    // ===============================================
    @GetMapping("/new")
    public String showNewTheaterForm(Model model) {
        // Provide an empty DTO to bind the form data to
        model.addAttribute("theaterDto", new TheaterDto());
        return "admin/theater/add_edit_theater";
    }

    // ===============================================
    // C - CREATE (Submit Form)
    // URL: /admin/theaters/new (POST)
    // ===============================================
    @PostMapping("/new")
    public String saveTheater(@Valid @ModelAttribute("theaterDto") TheaterDto theaterDto,
                              BindingResult result,
                              Model model) {

        if (result.hasErrors()) {
            return "admin/theater/add_edit_theater"; // Return to the form with errors
        }

        try {
            // Convert DTO to Entity
            Theater theater = new Theater(null, theaterDto.getName(), theaterDto.getCapacity(),
                    theaterDto.getLocationCity(), theaterDto.getScreenType());

            theaterService.createTheater(theater);

            // Redirect to the list view on success
            return "redirect:/admin/theaters/list?success";
        } catch (IllegalArgumentException e) {
            // Handle business rule violation (e.g., duplicate name/city)
            model.addAttribute("error", e.getMessage());
            return "admin/theater/add_edit_theater";
        }
    }

    // ===============================================
    // U - UPDATE (Show Edit Form)
    // URL: /admin/theaters/edit/{id}
    // ===============================================
    @GetMapping("/edit/{id}")
    public String showEditTheaterForm(@PathVariable("id") Long id, Model model) {
        Theater theater = theaterService.getTheaterById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid theater Id:" + id));

        // Convert Entity to DTO for form display
        TheaterDto theaterDto = new TheaterDto(theater.getTheaterId(), theater.getName(),
                theater.getCapacity(), theater.getLocationCity(),
                theater.getScreenType());

        model.addAttribute("theaterDto", theaterDto);
        return "admin/theater/add_edit_theater";
    }

    // ===============================================
    // U - UPDATE (Submit Edit Form)
    // URL: /admin/theaters/edit/{id} (POST)
    // ===============================================
    @PostMapping("/edit/{id}")
    public String updateTheater(@PathVariable("id") Long id,
                                @Valid @ModelAttribute("theaterDto") TheaterDto theaterDto,
                                BindingResult result,
                                Model model) {

        if (result.hasErrors()) {
            theaterDto.setTheaterId(id);
            return "admin/theater/add_edit_theater";
        }

        try {
            // Convert DTO back to Entity
            Theater theaterDetails = new Theater(id, theaterDto.getName(), theaterDto.getCapacity(),
                    theaterDto.getLocationCity(), theaterDto.getScreenType());

            theaterService.updateTheater(id, theaterDetails);
            return "redirect:/admin/theaters/list?updateSuccess";

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            theaterDto.setTheaterId(id);
            return "admin/theater/add_edit_theater";
        }
    }

    // ===============================================
    // D - DELETE Operation
    // URL: /admin/theaters/delete/{id}
    // ===============================================
    @GetMapping("/delete/{id}")
    public String deleteTheater(@PathVariable("id") Long id) {
        try {
            theaterService.deleteTheater(id);
            return "redirect:/admin/theaters/list?deleteSuccess";
        } catch (IllegalArgumentException e) {
            return "redirect:/admin/theaters/list?deleteError=" + id;
        }
    }
}