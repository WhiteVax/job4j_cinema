package ru.cinema.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;
import ru.cinema.model.Genre;

import java.util.List;
import java.util.Optional;

@Repository
public class Sql2oGenreRepository implements GenreRepository {

    private final Sql2o sql2o;

    public Sql2oGenreRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public List<Genre> findAll() {
        try (var connection = sql2o.open()) {
            return connection.createQuery("""
                    SELECT * FROM genres ORDER BY id
                    """).executeAndFetch(Genre.class);
        } catch (Sql2oException ex) {
            return List.of();
        }
    }

    @Override
    public Optional<Genre> findById(int id) {

        try (var connection = sql2o.open()) {
            var query = connection.createQuery("""
                            SELECT * FROM genres WHERE id = :id
                            """).addParameter("id", id);
            return Optional.ofNullable(query.executeAndFetchFirst(Genre.class));
        } catch (Sql2oException ex) {
            return Optional.empty();
        }
    }
}
