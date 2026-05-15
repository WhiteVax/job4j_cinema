package ru.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.cinema.service.FilmService;

@Controller
public class FilmController {
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public String films(Model model) {
        model.addAttribute("films", filmService.findAll());
        return "films";
    }

    @GetMapping("/films/search")
    public String search(@RequestParam(defaultValue = "") String key, Model model) {
        model.addAttribute("films", filmService.findByName(key));
        return "films";
    }

    @GetMapping("/films/{id}")
    public String film(@PathVariable int id, Model model) {
        var film = filmService.findById(id);
        if (film.isEmpty()) {
            return "redirect:/films";
        }
        model.addAttribute("film", film.get());
        return "film-details";
    }
}
