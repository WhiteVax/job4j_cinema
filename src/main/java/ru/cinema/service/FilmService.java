package ru.cinema.service;

import ru.cinema.dto.FilmDto;

import java.util.List;
import java.util.Optional;

public interface FilmService {
    List<FilmDto> findAll();

    List<FilmDto> findByName(String name);

    Optional<FilmDto> findById(int id);
}
