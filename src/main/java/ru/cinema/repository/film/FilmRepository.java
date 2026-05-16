package ru.cinema.repository.film;

import ru.cinema.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmRepository {
    Optional<Film> findById(int id);

    List<Film> findAll();

    List<Film> findByName(String name);
}
