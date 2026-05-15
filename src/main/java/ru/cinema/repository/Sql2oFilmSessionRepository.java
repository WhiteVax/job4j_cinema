package ru.cinema.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;
import ru.cinema.model.FilmSession;

import java.util.List;
import java.util.Optional;

@Repository
public class Sql2oFilmSessionRepository implements FilmSessionRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(Sql2oFilmSessionRepository.class);

    private final Sql2o sql2o;

    public Sql2oFilmSessionRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public List<FilmSession> findAll() {
        try (var connection = sql2o.open()) {
            return connection.createQuery("""
                            SELECT id, film_id AS filmId, halls_id AS hallsId,
                                   start_time AS startTime, end_time AS endTime,
                                   price
                            FROM film_sessions
                            ORDER BY start_time
                            """)
                    .executeAndFetch(FilmSession.class);
        } catch (Sql2oException ex) {
            LOGGER.error("Error when find all film sessions", ex);
            return List.of();
        }
    }

    @Override
    public Optional<FilmSession> findById(int id) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("""
                            SELECT id, film_id AS filmId, halls_id AS hallsId,
                                   start_time AS startTime, end_time AS endTime,
                                   price
                            FROM film_sessions
                            WHERE id = :id
                            """)
                    .addParameter("id", id);
            return Optional.ofNullable(query.executeAndFetchFirst(FilmSession.class));
        } catch (Sql2oException ex) {
            LOGGER.error("Error when find film session by id {}", id, ex);
            return Optional.empty();
        }
    }
}
