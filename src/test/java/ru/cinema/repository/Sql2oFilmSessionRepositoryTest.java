package ru.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;

import static org.assertj.core.api.Assertions.assertThat;

class Sql2oFilmSessionRepositoryTest {
    private static Sql2oFilmSessionRepository repository;
    private static Sql2o sql2o;

    @BeforeAll
    public static void initRepository() throws Exception {
        sql2o = RepositoryTestSupport.createSql2o();
        repository = new Sql2oFilmSessionRepository(sql2o);
    }

    @AfterEach
    public void clearTables() {
        RepositoryTestSupport.clearTables(sql2o);
    }

    @Test
    public void whenFindAllThenReturnSessions() {
        var sessionId = insertFilmSession();
        var sessions = repository.findAll();
        assertThat(sessions).hasSize(1);
        assertThat(sessions.get(0).getId()).isEqualTo(sessionId);
        assertThat(sessions.get(0).getPrice()).isEqualTo(300);
    }

    @Test
    public void whenFindByIdThenReturnSession() {
        var sessionId = insertFilmSession();
        var session = repository.findById(sessionId);
        assertThat(session).isPresent();
        assertThat(session.get().getId()).isEqualTo(sessionId);
        assertThat(session.get().getStartTime().getHour()).isEqualTo(18);
    }

    @Test
    public void whenFindByNotExistingIdThenReturnEmpty() {
        assertThat(repository.findById(999)).isEmpty();
    }

    private int insertFilmSession() {
        var genreId = RepositoryTestSupport.insertGenre(sql2o, "Adventure");
        var fileId = RepositoryTestSupport.insertFile(sql2o, "poster", "/adv.jpg");
        var filmId = RepositoryTestSupport.insertFilm(sql2o, genreId, fileId, "Avatar");
        var hallId = RepositoryTestSupport.insertHall(sql2o, "Hall");
        return RepositoryTestSupport.insertSession(sql2o, filmId, hallId);
    }
}
