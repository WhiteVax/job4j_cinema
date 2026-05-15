package ru.cinema.service;

import ru.cinema.dto.TicketDto;
import ru.cinema.model.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketService {
    Optional<Ticket> save(Ticket ticket);

    List<Ticket> findBySessionId(int sessionId);

    Optional<TicketDto> findDtoById(int id);
}
