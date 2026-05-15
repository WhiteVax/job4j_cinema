package ru.cinema.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;
import ru.cinema.model.Hall;

import java.util.Optional;

@Repository
public class Sql2oHallRepository implements HallRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(Sql2oHallRepository.class);
    private final Sql2o sql2o;

    public Sql2oHallRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<Hall> findById(int id) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("""
                            SELECT id, name, row_count AS rowCount,
                                   place_count AS placeCount, description
                            FROM halls
                            WHERE id = :id
                            """).addParameter("id", id);
            return Optional.ofNullable(query.executeAndFetchFirst(Hall.class));
        } catch (Sql2oException ex) {
            LOGGER.error("Error when findById hall", ex);
            return Optional.empty();
        }
    }
}
