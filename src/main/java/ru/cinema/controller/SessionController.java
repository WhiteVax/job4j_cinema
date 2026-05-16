package ru.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.cinema.service.session.SessionService;

@Controller
public class SessionController {
    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("/sessions")
    public String sessions(Model model) {
        model.addAttribute("sessions", sessionService.findAll());
        return "sessions";
    }

    @GetMapping("/sessions/{id}")
    public String session(@PathVariable int id, Model model) {
        var session = sessionService.findById(id);
        if (session.isEmpty()) {
            return "redirect:/sessions";
        }
        model.addAttribute("filmSession", session.get());
        return "session-details";
    }
}
