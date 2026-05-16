package ru.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.cinema.repository.film.Sql2oFilmRepository;

import static org.assertj.core.api.Assertions.assertThat;

class Sql2oFilmRepositoryTest {
    private static Sql2oFilmRepository repository;
    private static Sql2o sql2o;

    @BeforeAll
    public static void initRepository() throws Exception {
        sql2o = RepositoryTestSupport.createSql2o();
        repository = new Sql2oFilmRepository(sql2o);
    }

    @AfterEach
    public void clearTables() {
        RepositoryTestSupport.clearTables(sql2o);
    }

    @Test
    public void whenFindAllThenReturnFilms() {
        var genreId = RepositoryTestSupport.insertGenre(sql2o, "Sci-Fi");
        var fileId = RepositoryTestSupport.insertFile(sql2o, "poster", "/poster.jpg");
        RepositoryTestSupport.insertFilm(sql2o, genreId, fileId, "Interstellar");
        var films = repository.findAll();
        assertThat(films).hasSize(1);
        assertThat(films.get(0).getName()).isEqualTo("Interstellar");
        assertThat(films.get(0).getGenreId()).isEqualTo(genreId);
        assertThat(films.get(0).getFileId()).isEqualTo(fileId);
    }

    @Test
    public void whenFindByIdThenReturnFilm() {
        var genreId = RepositoryTestSupport.insertGenre(sql2o, "Action");
        var fileId = RepositoryTestSupport.insertFile(sql2o, "poster", "/action.jpg");
        var id = RepositoryTestSupport.insertFilm(sql2o, genreId, fileId, "Matrix");
        var film = repository.findById(id);
        assertThat(film).isPresent();
        assertThat(film.get().getName()).isEqualTo("Matrix");
        assertThat(film.get().getDurationInMinutes()).isEqualTo(120);
    }

    @Test
    public void whenFindByNameThenReturnMatchedFilms() {
        var genreId = RepositoryTestSupport.insertGenre(sql2o, "Drama");
        var fileId = RepositoryTestSupport.insertFile(sql2o, "poster", "/drama.jpg");
        RepositoryTestSupport.insertFilm(sql2o, genreId, fileId, "Green Book");
        var films = repository.findByName("green");
        assertThat(films).hasSize(1);
        assertThat(films.get(0).getName()).isEqualTo("Green Book");
    }
}
