package ru.cinema.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.cinema.model.User;

import java.io.IOException;
import java.util.Set;

@Component
@Order(2)
public class SessionFilter extends HttpFilter {
    private static final Set<String> PROTECTED_METHODS = Set.of("POST");

    @Override
    protected void doFilter(HttpServletRequest request,
                            HttpServletResponse response,
                            FilterChain chain) throws IOException, ServletException {
        var session = request.getSession();
        addUserToRequest(session, request);
        if (isProtectedTicketAction(request) && session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/users/login");
            return;
        }
        chain.doFilter(request, response);
    }

    private void addUserToRequest(HttpSession session, HttpServletRequest request) {
        var user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setFullName("Гость");
        }
        request.setAttribute("user", user);
    }

    private boolean isProtectedTicketAction(HttpServletRequest request) {
        return request.getRequestURI().contains("/tickets/buy")
                && PROTECTED_METHODS.contains(request.getMethod());
    }
}
