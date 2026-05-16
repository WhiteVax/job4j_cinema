package ru.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.cinema.model.Ticket;
import ru.cinema.repository.ticket.Sql2oTicketRepository;

import static org.assertj.core.api.Assertions.assertThat;

class Sql2oTicketRepositoryTest {
    private static Sql2oTicketRepository repository;
    private static Sql2o sql2o;

    @BeforeAll
    public static void initRepository() throws Exception {
        sql2o = RepositoryTestSupport.createSql2o();
        repository = new Sql2oTicketRepository(sql2o);
    }

    @AfterEach
    public void clearTables() {
        RepositoryTestSupport.clearTables(sql2o);
    }

    @Test
    public void whenSaveThenReturnTicketWithId() {
        var sessionId = insertFilmSession();
        var ticket = new Ticket(0, sessionId, 1, 2, 1);
        var saved = repository.save(ticket);
        assertThat(saved).isPresent();
        assertThat(saved.get().getId()).isGreaterThan(0);
    }

    @Test
    public void whenSaveSameSeatThenReturnEmpty() {
        var sessionId = insertFilmSession();
        repository.save(new Ticket(0, sessionId, 1, 2, 1));
        var duplicate = repository.save(new Ticket(0, sessionId, 1, 2, 2));
        assertThat(duplicate).isEmpty();
    }

    @Test
    public void whenFindBySessionIdThenReturnTickets() {
        var sessionId = insertFilmSession();
        repository.save(new Ticket(0, sessionId, 2, 3, 1));
        var tickets = repository.findBySessionId(sessionId);
        assertThat(tickets).hasSize(1);
        assertThat(tickets.get(0).getPlaceNumber()).isEqualTo(3);
    }

    @Test
    public void whenFindDtoByIdThenReturnTicketInfo() {
        var sessionId = insertFilmSession();
        var saved = repository.save(new Ticket(0, sessionId, 2, 3, 1));
        var dto = repository.findDtoById(saved.get().getId());
        assertThat(dto).isPresent();
        assertThat(dto.get().getFilmName()).isEqualTo("Avatar");
        assertThat(dto.get().getHallName()).isEqualTo("Hall");
    }

    private int insertFilmSession() {
        var genreId = RepositoryTestSupport.insertGenre(sql2o, "Adventure");
        var fileId = RepositoryTestSupport.insertFile(sql2o, "poster", "/adv.jpg");
        var filmId = RepositoryTestSupport.insertFilm(sql2o, genreId, fileId, "Avatar");
        var hallId = RepositoryTestSupport.insertHall(sql2o, "Hall");
        return RepositoryTestSupport.insertSession(sql2o, filmId, hallId);
    }
}
