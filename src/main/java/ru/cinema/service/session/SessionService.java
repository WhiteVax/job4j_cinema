package ru.cinema.service.session;

import ru.cinema.dto.SessionDto;

import java.util.List;
import java.util.Optional;

public interface SessionService {
    List<SessionDto> findAll();

    Optional<SessionDto> findById(int id);
}
