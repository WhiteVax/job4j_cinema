package ru.cinema.repository.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;
import ru.cinema.model.Film;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class Sql2oFilmRepository implements FilmRepository {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(Sql2oFilmRepository.class);

    private final Sql2o sql2o;

    public Sql2oFilmRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<Film> findById(int id) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("""
                            SELECT id, name, description, "year",
                                   genre_id AS genreId,
                                   minimal_age AS minimalAge,
                                   duration_in_minutes AS durationInMinutes,
                                   file_id AS fileId
                            FROM films
                            WHERE id = :id
                            """)
                    .addParameter("id", id);
            var film = query.executeAndFetchFirst(Film.class);
            return Optional.ofNullable(film);
        } catch (Sql2oException e) {
            LOGGER.error("Error when find id film", e);
            return Optional.empty();
        }
    }

    @Override
    public List<Film> findAll() {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("""
                    SELECT id, name, description, "year",
                           genre_id AS genreId,
                           minimal_age AS minimalAge,
                           duration_in_minutes AS durationInMinutes,
                           file_id AS fileId
                    FROM films
                    ORDER BY id
                    """);
            return query.executeAndFetch(Film.class);
        } catch (Sql2oException e) {
            LOGGER.error("Error when find all films", e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<Film> findByName(String name) {
        try (var connection = sql2o.open()) {

            var query = connection.createQuery("""
                            SELECT id, name, description, "year",
                                   genre_id AS genreId,
                                   minimal_age AS minimalAge,
                                   duration_in_minutes AS durationInMinutes,
                                   file_id AS fileId
                            FROM films
                            WHERE LOWER(name) LIKE LOWER(:name)
                            """)
                    .addParameter("name", "%" + name + "%");
            return query.executeAndFetch(Film.class);
        } catch (Sql2oException e) {
            LOGGER.error("Error with get all films", e);
            return Collections.emptyList();
        }
    }
}
