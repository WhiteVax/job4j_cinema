package ru.cinema.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.cinema.model.File;
import ru.cinema.model.Film;
import ru.cinema.model.Genre;
import ru.cinema.repository.FileRepository;
import ru.cinema.repository.FilmRepository;
import ru.cinema.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleFilmServiceTest {
    private FilmRepository filmRepository;
    private GenreRepository genreRepository;
    private FileRepository fileRepository;
    private SimpleFilmService filmService;

    @BeforeEach
    public void init() {
        filmRepository = mock(FilmRepository.class);
        genreRepository = mock(GenreRepository.class);
        fileRepository = mock(FileRepository.class);
        filmService = new SimpleFilmService(filmRepository, genreRepository, fileRepository);
    }

    @Test
    public void whenFindAllThenReturnFilmDtos() {
        when(filmRepository.findAll()).thenReturn(List.of(film()));
        configureRelatedRepositories();

        var films = filmService.findAll();

        assertThat(films).hasSize(1);
        assertThat(films.get(0).getGenre()).isEqualTo("Sci-Fi");
        assertThat(films.get(0).getPosterPath()).isEqualTo("/poster.jpg");
    }

    @Test
    public void whenFindByIdThenReturnFilmDto() {
        when(filmRepository.findById(1)).thenReturn(Optional.of(film()));
        configureRelatedRepositories();

        var film = filmService.findById(1);

        assertThat(film).isPresent();
        assertThat(film.get().getName()).isEqualTo("Matrix");
    }

    @Test
    public void whenFindByNameThenReturnFilmDtos() {
        when(filmRepository.findByName("mat")).thenReturn(List.of(film()));
        configureRelatedRepositories();

        var films = filmService.findByName("mat");

        assertThat(films).hasSize(1);
        assertThat(films.get(0).getMinimalAge()).isEqualTo(16);
    }

    private Film film() {
        return new Film(1, "Matrix", "Description", 1999, 2, 16, 136, 3);
    }

    private void configureRelatedRepositories() {
        when(genreRepository.findById(2)).thenReturn(Optional.of(new Genre(2, "Sci-Fi")));
        when(fileRepository.findById(3)).thenReturn(Optional.of(new File(3, "poster", "/poster.jpg")));
    }
}
