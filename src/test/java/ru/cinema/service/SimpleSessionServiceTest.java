package ru.cinema.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.cinema.model.Film;
import ru.cinema.model.FilmSession;
import ru.cinema.model.Hall;
import ru.cinema.repository.FilmRepository;
import ru.cinema.repository.FilmSessionRepository;
import ru.cinema.repository.HallRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleSessionServiceTest {
    private FilmSessionRepository sessionRepository;
    private FilmRepository filmRepository;
    private HallRepository hallRepository;
    private SimpleSessionService sessionService;

    @BeforeEach
    public void init() {
        sessionRepository = mock(FilmSessionRepository.class);
        filmRepository = mock(FilmRepository.class);
        hallRepository = mock(HallRepository.class);
        sessionService = new SimpleSessionService(sessionRepository, filmRepository, hallRepository);
    }

    @Test
    public void whenFindAllThenReturnSessionDtos() {
        when(sessionRepository.findAll()).thenReturn(List.of(session()));
        configureRelatedRepositories();

        var sessions = sessionService.findAll();

        assertThat(sessions).hasSize(1);
        assertThat(sessions.get(0).getFilmName()).isEqualTo("Matrix");
        assertThat(sessions.get(0).getHallName()).isEqualTo("Hall");
    }

    @Test
    public void whenFindByIdThenReturnSessionDto() {
        when(sessionRepository.findById(1)).thenReturn(Optional.of(session()));
        configureRelatedRepositories();

        var session = sessionService.findById(1);

        assertThat(session).isPresent();
        assertThat(session.get().getRowCount()).isEqualTo(5);
    }

    private FilmSession session() {
        return new FilmSession(1, 2, 3,
                LocalDateTime.of(2026, 5, 20, 18, 0),
                LocalDateTime.of(2026, 5, 20, 20, 0),
                300);
    }

    private void configureRelatedRepositories() {
        when(filmRepository.findById(2)).thenReturn(Optional.of(
                new Film(2, "Matrix", "Description", 1999, 1, 16, 136, 1)
        ));
        when(hallRepository.findById(3)).thenReturn(Optional.of(
                new Hall(3, "Hall", 5, 7, "Description")
        ));
    }
}
