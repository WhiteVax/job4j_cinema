package ru.cinema.repository;

import org.sql2o.Sql2o;
import ru.cinema.configuration.DatasourceConfiguration;

import java.time.LocalDateTime;
import java.util.Properties;

public final class RepositoryTestSupport {
    private RepositoryTestSupport() {
    }

    public static Sql2o createSql2o() throws Exception {
        var properties = new Properties();
        try (var inputStream = RepositoryTestSupport.class
                .getClassLoader()
                .getResourceAsStream("application-test.properties")) {
            properties.load(inputStream);
        }
        var configuration = new DatasourceConfiguration();
        var dataSource = configuration.connectionPool(
                properties.getProperty("datasource.url"),
                properties.getProperty("datasource.username"),
                properties.getProperty("datasource.password")
        );
        return configuration.dataBaseClient(dataSource);
    }

    public static void clearTables(Sql2o sql2o) {
        try (var connection = sql2o.open()) {
            connection.createQuery("DELETE FROM tickets").executeUpdate();
            connection.createQuery("DELETE FROM film_sessions").executeUpdate();
            connection.createQuery("DELETE FROM films").executeUpdate();
            connection.createQuery("DELETE FROM halls").executeUpdate();
            connection.createQuery("DELETE FROM genres").executeUpdate();
            connection.createQuery("DELETE FROM files").executeUpdate();
            connection.createQuery("DELETE FROM users").executeUpdate();
        }
    }

    public static int insertFile(Sql2o sql2o, String name, String path) {
        try (var connection = sql2o.open()) {
            return connection.createQuery("""
                            INSERT INTO files(name, path) VALUES (:name, :path)
                            """, true)
                    .addParameter("name", name)
                    .addParameter("path", path)
                    .executeUpdate()
                    .getKey(Integer.class);
        }
    }

    public static int insertGenre(Sql2o sql2o, String name) {
        try (var connection = sql2o.open()) {
            return connection.createQuery("""
                            INSERT INTO genres(name) VALUES (:name)
                            """, true)
                    .addParameter("name", name)
                    .executeUpdate()
                    .getKey(Integer.class);
        }
    }

    public static int insertHall(Sql2o sql2o, String name) {
        try (var connection = sql2o.open()) {
            return connection.createQuery("""
                            INSERT INTO halls(name, row_count, place_count, description)
                            VALUES (:name, 3, 4, 'Test hall')
                            """, true)
                    .addParameter("name", name)
                    .executeUpdate()
                    .getKey(Integer.class);
        }
    }

    public static int insertFilm(Sql2o sql2o, int genreId, int fileId, String name) {
        try (var connection = sql2o.open()) {
            return connection.createQuery("""
                            INSERT INTO films(name, description, "year", genre_id,
                                              minimal_age, duration_in_minutes, file_id)
                            VALUES (:name, 'Description', 2024, :genreId, 12, 120, :fileId)
                            """, true)
                    .addParameter("name", name)
                    .addParameter("genreId", genreId)
                    .addParameter("fileId", fileId)
                    .executeUpdate()
                    .getKey(Integer.class);
        }
    }

    public static int insertSession(Sql2o sql2o, int filmId, int hallId) {
        try (var connection = sql2o.open()) {
            return connection.createQuery("""
                            INSERT INTO film_sessions(film_id, halls_id, start_time, end_time, price)
                            VALUES (:filmId, :hallId, :startTime, :endTime, 300)
                            """, true)
                    .addParameter("filmId", filmId)
                    .addParameter("hallId", hallId)
                    .addParameter("startTime", LocalDateTime.of(2026, 5, 20, 18, 0))
                    .addParameter("endTime", LocalDateTime.of(2026, 5, 20, 20, 0))
                    .executeUpdate()
                    .getKey(Integer.class);
        }
    }
}
