package ru.cinema.repository.ticket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;
import ru.cinema.dto.TicketDto;
import ru.cinema.model.Ticket;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class Sql2oTicketRepository implements TicketRepository {

    private static final String UNIQUE_CODE_TICKET = "23505";
    private static final Logger LOGGER = LoggerFactory.getLogger(Sql2oTicketRepository.class);
    private final Sql2o sql2o;

    public Sql2oTicketRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<Ticket> save(Ticket ticket) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("""
                            INSERT INTO tickets(session_id, row_number, place_number, user_id)
                                                   VALUES (:sessionId, :rowNumber, :placeNumber, :userId)
                            """, true)
                    .bind(ticket);

            int generatedId = query.executeUpdate().getKey(Integer.class);
            ticket.setId(generatedId);
            return Optional.of(ticket);
        } catch (Sql2oException e) {
            if (e.getCause() instanceof SQLException sqlException
                    && UNIQUE_CODE_TICKET.equals(sqlException.getSQLState())) {
                LOGGER.warn("Ticket for this place already exists");
            } else {
                LOGGER.error("Error while saving ticket", e);
            }
            return Optional.empty();
        }
    }

    @Override
    public List<Ticket> findBySessionId(int sessionId) {
        try (var connection = sql2o.open()) {
            return connection.createQuery("""
                            SELECT id, session_id AS sessionId,
                                   row_number AS rowNumber,
                                   place_number AS placeNumber,
                                   user_id AS userId
                            FROM tickets
                            WHERE session_id = :sessionId
                            """)
                    .addParameter("sessionId", sessionId)
                    .executeAndFetch(Ticket.class);
        } catch (Sql2oException e) {
            LOGGER.error("Error while finding tickets", e);
            return List.of();
        }
    }

    @Override
    public Optional<TicketDto> findDtoById(int id) {
        try (var connection = sql2o.open()) {
            var ticket = connection.createQuery("""
                            SELECT t.id, t.session_id AS sessionId,
                                   f.name AS filmName, h.name AS hallName,
                                   t.row_number AS rowNumber,
                                   t.place_number AS placeNumber,
                                   fs.price
                            FROM tickets t
                            JOIN film_sessions fs ON fs.id = t.session_id
                            JOIN films f ON f.id = fs.film_id
                            JOIN halls h ON h.id = fs.halls_id
                            WHERE t.id = :id
                            """)
                    .addParameter("id", id)
                    .executeAndFetchFirst(TicketDto.class);
            return Optional.ofNullable(ticket);
        } catch (Sql2oException e) {
            LOGGER.error("Error while finding ticket dto", e);
            return Optional.empty();
        }
    }
}
