package ru.cinema.service;

import org.springframework.stereotype.Service;
import ru.cinema.dto.SessionDto;
import ru.cinema.model.Film;
import ru.cinema.model.FilmSession;
import ru.cinema.repository.FilmRepository;
import ru.cinema.repository.FilmSessionRepository;
import ru.cinema.repository.HallRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SimpleSessionService implements SessionService {
    private final FilmSessionRepository sessionRepository;
    private final FilmRepository filmRepository;
    private final HallRepository hallRepository;

    public SimpleSessionService(FilmSessionRepository sessionRepository,
                                FilmRepository filmRepository,
                                HallRepository hallRepository) {
        this.sessionRepository = sessionRepository;
        this.filmRepository = filmRepository;
        this.hallRepository = hallRepository;
    }

    @Override
    public List<SessionDto> findAll() {
        return sessionRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public Optional<SessionDto> findById(int id) {
        return sessionRepository.findById(id).map(this::toDto);
    }

    private SessionDto toDto(FilmSession session) {
        var film = filmRepository.findById(session.getFilmId());
        var hall = hallRepository.findById(session.getHallsId());
        return SessionDto.builder()
                .id(session.getId())
                .filmName(film.map(Film::getName).orElse(""))
                .filmDescription(film.map(ru.cinema.model.Film::getDescription).orElse(""))
                .hallName(hall.map(ru.cinema.model.Hall::getName).orElse(""))
                .rowCount(hall.map(ru.cinema.model.Hall::getRowCount).orElse(0))
                .placeCount(hall.map(ru.cinema.model.Hall::getPlaceCount).orElse(0))
                .startTime(session.getStartTime())
                .endTime(session.getEndTime())
                .price(session.getPrice())
                .build();
    }
}
