package ru.cinema.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.cinema.model.User;
import ru.cinema.service.user.UserService;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"/register", "/registration"})
    public String registration() {
        return "register";
    }

    @PostMapping("/register")
    public String register(User user, Model model) {
        var result = userService.save(user);
        if (result.isEmpty()) {
            model.addAttribute("error", "Пользователь с такой почтой уже существует");
            return "register";
        }
        return "redirect:/users/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(String email, String password, Model model, HttpSession session) {
        var user = userService.findByEmailAndPassword(email, password);
        if (user.isEmpty()) {
            model.addAttribute("error", "Неверная почта или пароль");
            return "login";
        }
        session.setAttribute("user", user.get());
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
