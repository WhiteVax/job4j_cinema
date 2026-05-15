package ru.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;

import static org.assertj.core.api.Assertions.assertThat;

class Sql2oGenreRepositoryTest {
    private static Sql2oGenreRepository repository;
    private static Sql2o sql2o;

    @BeforeAll
    public static void initRepository() throws Exception {
        sql2o = RepositoryTestSupport.createSql2o();
        repository = new Sql2oGenreRepository(sql2o);
    }

    @AfterEach
    public void clearTables() {
        RepositoryTestSupport.clearTables(sql2o);
    }

    @Test
    public void whenFindAllThenReturnGenres() {
        RepositoryTestSupport.insertGenre(sql2o, "Drama");
        RepositoryTestSupport.insertGenre(sql2o, "Comedy");
        var genres = repository.findAll();
        assertThat(genres).hasSize(2);
        assertThat(genres).extracting("name").containsExactly("Drama", "Comedy");
    }

    @Test
    public void whenFindByIdThenReturnGenre() {
        var id = RepositoryTestSupport.insertGenre(sql2o, "Action");
        var genre = repository.findById(id);
        assertThat(genre).isPresent();
        assertThat(genre.get().getName()).isEqualTo("Action");
    }

    @Test
    public void whenFindByNotExistingIdThenReturnEmpty() {
        assertThat(repository.findById(999)).isEmpty();
    }
}
