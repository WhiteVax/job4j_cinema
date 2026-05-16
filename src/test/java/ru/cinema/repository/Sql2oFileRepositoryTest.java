package ru.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.cinema.configuration.DatasourceConfiguration;
import ru.cinema.model.File;
import ru.cinema.repository.file.Sql2oFileRepository;

import java.util.Optional;
import java.util.Properties;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oFileRepositoryTest {

    private static Sql2oFileRepository sql2oFileRepository;
    private static Sql2o sql2o;

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oFileRepositoryTest.class
                .getClassLoader()
                .getResourceAsStream("application-test.properties")) {

            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");
        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        sql2o = configuration.dataBaseClient(datasource);
        sql2oFileRepository = new Sql2oFileRepository(sql2o);
    }

    @AfterEach
    public void clearTable() {
        try (var connection = sql2o.open()) {
            connection.createQuery("""
                    DELETE FROM files
                    """).executeUpdate();
        }
    }

    @Test
    public void whenSaveFileThenFileGetsId() {
        File file = new File();
        file.setName("avatar.png");
        file.setPath("/images/avatar.png");
        sql2oFileRepository.save(file);
        assertThat(file.getId()).isGreaterThan(0);
        assertThat(file.getName()).isEqualTo("avatar.png");
        assertThat(file.getPath()).isEqualTo("/images/avatar.png");
    }

    @Test
    public void whenFindByIdThenReturnFile() {
        File file = new File();
        file.setName("poster.jpg");
        file.setPath("/images/poster.jpg");
        sql2oFileRepository.save(file);
        Optional<File> foundFile = sql2oFileRepository.findById(file.getId());
        assertThat(foundFile).isPresent();
        assertThat(foundFile.get().getId()).isEqualTo(file.getId());
        assertThat(foundFile.get().getName()).isEqualTo(file.getName());
        assertThat(foundFile.get().getPath()).isEqualTo(file.getPath());
    }

    @Test
    public void whenFindByNonExistingIdThenReturnEmptyOptional() {
        Optional<File> foundFile = sql2oFileRepository.findById(999);
        assertThat(foundFile).isEmpty();
    }
}