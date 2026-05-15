package ru.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import ru.cinema.dto.SessionDto;
import ru.cinema.service.SessionService;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class SessionControllerTest {
    private SessionService sessionService;
    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        sessionService = mock(SessionService.class);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new SessionController(sessionService))
                .setViewResolvers(viewResolver())
                .build();
    }

    @Test
    public void whenGetSessionsThenReturnSessionsView() throws Exception {
        when(sessionService.findAll()).thenReturn(List.of(new SessionDto()));

        mockMvc.perform(get("/sessions"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("sessions"))
                .andExpect(view().name("sessions"));
    }

    @Test
    public void whenGetSessionByIdThenReturnSessionDetailsView() throws Exception {
        when(sessionService.findById(1)).thenReturn(Optional.of(new SessionDto()));

        mockMvc.perform(get("/sessions/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("filmSession"))
                .andExpect(view().name("session-details"));
    }

    @Test
    public void whenGetNotExistingSessionThenRedirectToSessions() throws Exception {
        when(sessionService.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/sessions/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/sessions"));
    }

    private InternalResourceViewResolver viewResolver() {
        return new InternalResourceViewResolver("/WEB-INF/", ".jsp");
    }
}
