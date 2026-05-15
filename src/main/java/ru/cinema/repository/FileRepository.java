package ru.cinema.repository;

import ru.cinema.model.File;

import java.util.Optional;

public interface FileRepository {
    void save(File file);

    Optional<File> findById(int id);
}
