package ru.cinema.repository.genre;

import ru.cinema.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {

    List<Genre> findAll();

    Optional<Genre> findById(int id);
}
