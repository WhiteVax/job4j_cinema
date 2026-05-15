package ru.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;

import static org.assertj.core.api.Assertions.assertThat;

class Sql2oHallRepositoryTest {
    private static Sql2oHallRepository repository;
    private static Sql2o sql2o;

    @BeforeAll
    public static void initRepository() throws Exception {
        sql2o = RepositoryTestSupport.createSql2o();
        repository = new Sql2oHallRepository(sql2o);
    }

    @AfterEach
    public void clearTables() {
        RepositoryTestSupport.clearTables(sql2o);
    }

    @Test
    public void whenFindByIdThenReturnHall() {
        var id = RepositoryTestSupport.insertHall(sql2o, "Hall A");
        var hall = repository.findById(id);
        assertThat(hall).isPresent();
        assertThat(hall.get().getName()).isEqualTo("Hall A");
        assertThat(hall.get().getRowCount()).isEqualTo(3);
        assertThat(hall.get().getPlaceCount()).isEqualTo(4);
    }

    @Test
    public void whenFindByNotExistingIdThenReturnEmpty() {
        assertThat(repository.findById(999)).isEmpty();
    }
}
