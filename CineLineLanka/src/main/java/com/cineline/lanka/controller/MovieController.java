package com.cineline.lanka.controller;

import com.cineline.lanka.dto.MovieDto;
import com.cineline.lanka.model.movie.Movie;
import com.cineline.lanka.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@Controller
@RequestMapping("/admin/movies") // Base path for all movie management URLs
public class MovieController {

    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    // ===============================================
    // R - READ (List All Movies)
    // URL: /admin/movies/list
    // ===============================================
    @GetMapping("/list")
    public String listMovies(Model model) {
        List<Movie> movies = movieService.getAllMovies();
        model.addAttribute("movies", movies);
        return "admin/movie/list_movies"; // Thymeleaf template location
    }

    // ===============================================
    // C - CREATE (Show Form)
    // URL: /admin/movies/new
    // ===============================================
    @GetMapping("/new")
    public String showNewMovieForm(Model model) {
        // Provide an empty DTO to bind the form data to
        model.addAttribute("movieDto", new MovieDto());
        return "admin/movie/add_edit_movie";
    }

    // ===============================================
    // C - CREATE (Submit Form)
    // URL: /admin/movies/new (POST)
    // ===============================================
    @PostMapping("/new")
    public String saveMovie(@Valid @ModelAttribute("movieDto") MovieDto movieDto,
                            BindingResult result,
                            Model model) {

        if (result.hasErrors()) {
            return "admin/movie/add_edit_movie"; // Return to the form with errors
        }

        try {
            // Convert DTO to Entity (Manual mapping for simplicity)
            Movie movie = new Movie(null, movieDto.getTitle(), movieDto.getGenre(),
                    movieDto.getDuration(), movieDto.getReleaseDate());

            movieService.createMovie(movie);

            // Redirect to the list view on success
            return "redirect:/admin/movies/list?success";
        } catch (IllegalArgumentException e) {
            // Handle business rule violation (e.g., duplicate title)
            model.addAttribute("error", e.getMessage());
            return "admin/movie/add_edit_movie";
        }
    }

    // ===============================================
    // U - UPDATE (Show Edit Form)
    // URL: /admin/movies/edit/{id}
    // ===============================================
    @GetMapping("/edit/{id}")
    public String showEditMovieForm(@PathVariable("id") Long id, Model model) {
        Movie movie = movieService.getMovieById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid movie Id:" + id));

        // Convert Entity to DTO for form display
        MovieDto movieDto = new MovieDto(movie.getMovieId(), movie.getTitle(), movie.getGenre(),
                movie.getDuration(), movie.getReleaseDate());

        model.addAttribute("movieDto", movieDto);
        return "admin/movie/add_edit_movie";
    }

    // ===============================================
    // U - UPDATE (Submit Edit Form)
    // URL: /admin/movies/edit/{id} (POST)
    // ===============================================
    @PostMapping("/edit/{id}")
    public String updateMovie(@PathVariable("id") Long id,
                              @Valid @ModelAttribute("movieDto") MovieDto movieDto,
                              BindingResult result,
                              Model model) {

        if (result.hasErrors()) {
            // Ensure ID is passed back for re-displaying the form
            movieDto.setMovieId(id);
            return "admin/movie/add_edit_movie";
        }

        try {
            // Convert DTO back to Entity
            Movie movieDetails = new Movie(id, movieDto.getTitle(), movieDto.getGenre(),
                    movieDto.getDuration(), movieDto.getReleaseDate());

            movieService.updateMovie(id, movieDetails);
            return "redirect:/admin/movies/list?updateSuccess";

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            movieDto.setMovieId(id);
            return "admin/movie/add_edit_movie";
        }
    }

    // ===============================================
    // D - DELETE Operation
    // URL: /admin/movies/delete/{id}
    // ===============================================
    @GetMapping("/delete/{id}")
    public String deleteMovie(@PathVariable("id") Long id) {
        try {
            movieService.deleteMovie(id);
            return "redirect:/admin/movies/list?deleteSuccess";
        } catch (IllegalArgumentException e) {
            // Handle case where movie isn't found
            return "redirect:/admin/movies/list?deleteError=" + id;
        }
    }
}