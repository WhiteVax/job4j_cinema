package ru.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import ru.cinema.dto.FilmDto;
import ru.cinema.service.FilmService;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class FilmControllerTest {
    private FilmService filmService;
    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        filmService = mock(FilmService.class);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new FilmController(filmService))
                .setViewResolvers(viewResolver())
                .build();
    }

    @Test
    public void whenGetFilmsThenReturnFilmsView() throws Exception {
        when(filmService.findAll()).thenReturn(List.of(new FilmDto()));

        mockMvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("films"))
                .andExpect(view().name("films"));
    }

    @Test
    public void whenSearchThenReturnFilmsView() throws Exception {
        when(filmService.findByName("matrix")).thenReturn(List.of(new FilmDto()));

        mockMvc.perform(get("/films/search").param("key", "matrix"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("films"))
                .andExpect(view().name("films"));
    }

    @Test
    public void whenGetFilmByIdThenReturnFilmDetailsView() throws Exception {
        when(filmService.findById(1)).thenReturn(Optional.of(new FilmDto()));

        mockMvc.perform(get("/films/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("film"))
                .andExpect(view().name("film-details"));
    }

    @Test
    public void whenGetNotExistingFilmThenRedirectToFilms() throws Exception {
        when(filmService.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/films/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/films"));
    }

    private InternalResourceViewResolver viewResolver() {
        return new InternalResourceViewResolver("/WEB-INF/", ".jsp");
    }
}
