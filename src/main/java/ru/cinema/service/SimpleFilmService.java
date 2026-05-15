package ru.cinema.service;

import org.springframework.stereotype.Service;
import ru.cinema.dto.FilmDto;
import ru.cinema.model.File;
import ru.cinema.model.Film;
import ru.cinema.model.Genre;
import ru.cinema.repository.FileRepository;
import ru.cinema.repository.FilmRepository;
import ru.cinema.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SimpleFilmService implements FilmService {
    private final FilmRepository filmRepository;
    private final GenreRepository genreRepository;
    private final FileRepository fileRepository;

    public SimpleFilmService(FilmRepository filmRepository, GenreRepository genreRepository,
                             FileRepository fileRepository) {
        this.filmRepository = filmRepository;
        this.genreRepository = genreRepository;
        this.fileRepository = fileRepository;
    }

    @Override
    public List<FilmDto> findAll() {
        return filmRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public List<FilmDto> findByName(String name) {
        return filmRepository.findByName(name).stream().map(this::toDto).toList();
    }

    @Override
    public Optional<FilmDto> findById(int id) {
        return filmRepository.findById(id).map(this::toDto);
    }

    private FilmDto toDto(Film film) {
        var genre = genreRepository.findById(film.getGenreId())
                .map(Genre::getName)
                .orElse("Неизвестно");
        var poster = fileRepository.findById(film.getFileId())
                .map(File::getPath)
                .orElse("");
        return FilmDto.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .year(film.getYear())
                .minimalAge(film.getMinimalAge())
                .durationInMinutes(film.getDurationInMinutes())
                .genre(genre)
                .posterPath(poster)
                .build();
    }
}
