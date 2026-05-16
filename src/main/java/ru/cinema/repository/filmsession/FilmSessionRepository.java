package ru.cinema.repository.filmsession;

import ru.cinema.model.FilmSession;

import java.util.List;
import java.util.Optional;

public interface FilmSessionRepository {

    List<FilmSession> findAll();

    Optional<FilmSession> findById(int id);
}
