package ru.cinema.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.cinema.model.Ticket;
import ru.cinema.model.User;
import ru.cinema.service.ticket.TicketService;

@Controller
@RequestMapping("/tickets")
public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/buy")
    public String buy(Ticket ticket, HttpSession session) {
        var user = (User) session.getAttribute("user");
        ticket.setUserId(user.getId());
        var saved = ticketService.save(ticket);
        if (saved.isEmpty()) {
            return "redirect:/tickets/fail";
        }
        return "redirect:/tickets/success?id=" + saved.get().getId();
    }

    @GetMapping("/success")
    public String success(int id, Model model) {
        var ticket = ticketService.findDtoById(id);
        if (ticket.isEmpty()) {
            return "redirect:/sessions";
        }
        model.addAttribute("ticket", ticket.get());
        return "ticket-success";
    }

    @GetMapping("/fail")
    public String fail() {
        return "ticket-fail";
    }
}
