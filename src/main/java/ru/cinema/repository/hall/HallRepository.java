package ru.cinema.repository.hall;

import ru.cinema.model.Hall;

import java.util.Optional;

public interface HallRepository {
    Optional<Hall> findById(int id);
}
