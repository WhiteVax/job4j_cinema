package ru.cinema.repository;

import ru.cinema.model.Hall;

import java.util.Optional;

public interface HallRepository {
    Optional<Hall> findById(int id);
}
