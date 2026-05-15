package ru.cinema.service;

import org.springframework.stereotype.Service;
import ru.cinema.dto.TicketDto;
import ru.cinema.model.Ticket;
import ru.cinema.repository.TicketRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SimpleTicketService implements TicketService {
    private final TicketRepository ticketRepository;

    public SimpleTicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Optional<Ticket> save(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public List<Ticket> findBySessionId(int sessionId) {
        return ticketRepository.findBySessionId(sessionId);
    }

    @Override
    public Optional<TicketDto> findDtoById(int id) {
        return ticketRepository.findDtoById(id);
    }
}
