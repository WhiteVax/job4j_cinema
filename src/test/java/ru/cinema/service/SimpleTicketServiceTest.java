package ru.cinema.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.cinema.dto.TicketDto;
import ru.cinema.model.Ticket;
import ru.cinema.repository.ticket.TicketRepository;
import ru.cinema.service.ticket.SimpleTicketService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleTicketServiceTest {
    private TicketRepository ticketRepository;
    private SimpleTicketService ticketService;

    @BeforeEach
    public void init() {
        ticketRepository = mock(TicketRepository.class);
        ticketService = new SimpleTicketService(ticketRepository);
    }

    @Test
    public void whenSaveThenReturnTicket() {
        var ticket = new Ticket(0, 1, 1, 2, 1);
        when(ticketRepository.save(ticket)).thenReturn(Optional.of(ticket));

        assertThat(ticketService.save(ticket)).isPresent();
    }

    @Test
    public void whenFindBySessionIdThenReturnTickets() {
        when(ticketRepository.findBySessionId(1))
                .thenReturn(List.of(new Ticket(1, 1, 1, 2, 1)));

        var tickets = ticketService.findBySessionId(1);

        assertThat(tickets).hasSize(1);
        assertThat(tickets.get(0).getPlaceNumber()).isEqualTo(2);
    }

    @Test
    public void whenFindDtoByIdThenReturnTicketDto() {
        when(ticketRepository.findDtoById(1)).thenReturn(Optional.of(new TicketDto()));

        assertThat(ticketService.findDtoById(1)).isPresent();
    }
}
