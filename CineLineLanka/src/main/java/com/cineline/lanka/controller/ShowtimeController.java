package com.cineline.lanka.controller;

import com.cineline.lanka.dto.ShowtimeDto;
import com.cineline.lanka.model.movie.Movie;
import com.cineline.lanka.model.showtime.Showtime;
import com.cineline.lanka.model.theater.Theater;
import com.cineline.lanka.service.MovieService;
import com.cineline.lanka.service.ShowtimeService;
import com.cineline.lanka.service.TheaterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/showtimes") // Base path
public class ShowtimeController {

    private final ShowtimeService showtimeService;
    private final MovieService movieService;
    private final TheaterService theaterService;

    @Autowired
    public ShowtimeController(ShowtimeService showtimeService,
                              MovieService movieService,
                              TheaterService theaterService) {
        this.showtimeService = showtimeService;
        this.movieService = movieService;
        this.theaterService = theaterService;
    }

    /**
     * Helper method to add required lists (Movies, Theaters) to the model.
     */
    private void addDropdownDataToModel(Model model) {
        model.addAttribute("movies", movieService.getAllMovies());
        model.addAttribute("theaters", theaterService.getAllTheaters());
    }

    // ===============================================
    // R - READ (List All Showtimes)
    // URL: /admin/showtimes/list
    // ===============================================
    @GetMapping("/list")
    public String listShowtimes(Model model) {
        List<Showtime> showtimes = showtimeService.getAllShowtimes();
        model.addAttribute("showtimes", showtimes);
        // FIX APPLIED HERE: Added the full path "admin/showtime/"
        return "admin/showtime/list_showtimes";
    }

    // ===============================================
    // C - CREATE (Show Form)
    // URL: /admin/showtimes/new
    // ===============================================
    @GetMapping("/new")
    public String showNewShowtimeForm(Model model) {
        model.addAttribute("showtimeDto", new ShowtimeDto());
        addDropdownDataToModel(model); // Populate dropdowns
        return "admin/showtime/add_edit_showtime";
    }

    // ===============================================
    // C - CREATE (Submit Form)
    // URL: /admin/showtimes/new (POST)
    // ===============================================
    @PostMapping("/new")
    public String saveShowtime(@Valid @ModelAttribute("showtimeDto") ShowtimeDto showtimeDto,
                               BindingResult result,
                               Model model) {
        if (result.hasErrors()) {
            addDropdownDataToModel(model); // Must re-add dropdown data if returning to form
            return "admin/showtime/add_edit_showtime";
        }

        try {
            // Convert DTO to Entity for service layer
            Showtime showtime = convertDtoToEntity(showtimeDto);
            showtimeService.createShowtime(showtime);

            return "redirect:/admin/showtimes/list?success";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            addDropdownDataToModel(model);
            return "admin/showtime/add_edit_showtime";
        }
    }

    // ===============================================
    // U - UPDATE (Show Edit Form)
    // URL: /admin/showtimes/edit/{id}
    // ===============================================
    @GetMapping("/edit/{id}")
    public String showEditShowtimeForm(@PathVariable("id") Long id, Model model) {
        Showtime showtime = showtimeService.getShowtimeById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid showtime Id:" + id));

        // Convert Entity to DTO for form display
        ShowtimeDto showtimeDto = convertEntityToDto(showtime);

        model.addAttribute("showtimeDto", showtimeDto);
        addDropdownDataToModel(model);
        return "admin/showtime/add_edit_showtime";
    }

    // ===============================================
    // U - UPDATE (Submit Edit Form)
    // URL: /admin/showtimes/edit/{id} (POST)
    // ===============================================
    @PostMapping("/edit/{id}")
    public String updateShowtime(@PathVariable("id") Long id,
                                 @Valid @ModelAttribute("showtimeDto") ShowtimeDto showtimeDto,
                                 BindingResult result,
                                 Model model) {
        if (result.hasErrors()) {
            showtimeDto.setShowtimeId(id);
            addDropdownDataToModel(model);
            return "admin/showtime/add_edit_showtime";
        }

        try {
            Showtime showtimeDetails = convertDtoToEntity(showtimeDto);
            showtimeService.updateShowtime(id, showtimeDetails);
            return "redirect:/admin/showtimes/list?updateSuccess";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            showtimeDto.setShowtimeId(id);
            addDropdownDataToModel(model);
            return "admin/showtime/add_edit_showtime";
        }
    }

    // ===============================================
    // D - DELETE Operation
    // URL: /admin/showtimes/delete/{id}
    // ===============================================
    @GetMapping("/delete/{id}")
    public String deleteShowtime(@PathVariable("id") Long id) {
        try {
            showtimeService.deleteShowtime(id);
            return "redirect:/admin/showtimes/list?deleteSuccess";
        } catch (IllegalArgumentException e) {
            return "redirect:/admin/showtimes/list?deleteError=" + id;
        }
    }

    // ===============================================
    // DTO <-> Entity Conversion Helpers
    // ===============================================
    private Showtime convertDtoToEntity(ShowtimeDto dto) {
        // Need to create placeholder entities with only the ID set for the service layer lookup
        Movie movie = new Movie();
        movie.setMovieId(dto.getMovieId());

        Theater theater = new Theater();
        theater.setTheaterId(dto.getTheaterId());

        return new Showtime(dto.getShowtimeId(), movie, theater, dto.getStartTime(),
                null, dto.getTicketPrice());
    }

    private ShowtimeDto convertEntityToDto(Showtime entity) {
        return new ShowtimeDto(
                entity.getShowtimeId(),
                entity.getMovie().getMovieId(),
                entity.getTheater().getTheaterId(),
                entity.getStartTime(),
                entity.getTicketPrice()
        );
    }
}