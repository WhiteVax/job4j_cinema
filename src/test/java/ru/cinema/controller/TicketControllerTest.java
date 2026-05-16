package ru.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import ru.cinema.dto.TicketDto;
import ru.cinema.model.Ticket;
import ru.cinema.model.User;
import ru.cinema.service.ticket.TicketService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class TicketControllerTest {
    private TicketService ticketService;
    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        ticketService = mock(TicketService.class);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new TicketController(ticketService))
                .setViewResolvers(viewResolver())
                .build();
    }

    @Test
    public void whenBuySuccessThenRedirectToSuccess() throws Exception {
        var session = sessionWithUser();
        when(ticketService.save(any(Ticket.class)))
                .thenReturn(Optional.of(new Ticket(5, 1, 1, 2, 1)));

        mockMvc.perform(post("/tickets/buy")
                        .session(session)
                        .param("sessionId", "1")
                        .param("rowNumber", "1")
                        .param("placeNumber", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tickets/success?id=5"));
    }

    @Test
    public void whenBuyFailsThenRedirectToFail() throws Exception {
        when(ticketService.save(any(Ticket.class))).thenReturn(Optional.empty());

        mockMvc.perform(post("/tickets/buy")
                        .session(sessionWithUser())
                        .param("sessionId", "1")
                        .param("rowNumber", "1")
                        .param("placeNumber", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tickets/fail"));
    }

    @Test
    public void whenSuccessThenReturnTicketSuccessView() throws Exception {
        when(ticketService.findDtoById(5)).thenReturn(Optional.of(new TicketDto()));

        mockMvc.perform(get("/tickets/success").param("id", "5"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("ticket"))
                .andExpect(view().name("ticket-success"));
    }

    @Test
    public void whenSuccessWithNotExistingTicketThenRedirectToSessions() throws Exception {
        when(ticketService.findDtoById(5)).thenReturn(Optional.empty());

        mockMvc.perform(get("/tickets/success").param("id", "5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/sessions"));
    }

    @Test
    public void whenFailThenReturnTicketFailView() throws Exception {
        mockMvc.perform(get("/tickets/fail"))
                .andExpect(status().isOk())
                .andExpect(view().name("ticket-fail"));
    }

    private MockHttpSession sessionWithUser() {
        var session = new MockHttpSession();
        session.setAttribute("user", new User(1, "Ivan", "ivan@test.com", "123"));
        return session;
    }

    private InternalResourceViewResolver viewResolver() {
        return new InternalResourceViewResolver("/WEB-INF/", ".jsp");
    }
}
