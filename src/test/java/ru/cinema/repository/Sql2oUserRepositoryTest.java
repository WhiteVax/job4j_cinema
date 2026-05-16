package ru.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.cinema.configuration.DatasourceConfiguration;
import ru.cinema.model.User;
import ru.cinema.repository.user.Sql2oUserRepository;

import java.util.Optional;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

class Sql2oUserRepositoryTest {
    private static Sql2oUserRepository sql2oUser;
    private static Sql2o sql2o;

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oUserRepositoryTest.class.getClassLoader().getResourceAsStream("application-test.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        sql2o = configuration.dataBaseClient(datasource);

        sql2oUser = new Sql2oUserRepository(sql2o);
    }

    @AfterEach
    public void clearUsers() {
        try (var connection = sql2o.open()) {
            connection.createQuery("TRUNCATE TABLE users RESTART IDENTITY").executeUpdate();
        }
    }

    @Test
    public void whenSaveUserThenReturnUserWithId() {
        User user = new User();
        user.setFullName("Vova");
        user.setEmail("vova23@test.com");
        user.setPassword("123qw3");

        Optional<User> savedUser = sql2oUser.save(user);

        assertThat(savedUser).isPresent();
        assertThat(savedUser.get().getId()).isGreaterThan(0);
        assertThat(savedUser.get().getEmail()).isEqualTo("vova23@test.com");
    }

    @Test
    public void whenSaveUserThenFindUserByEmailAndPassword() {
        User user = new User();
        user.setFullName("Vova");
        user.setEmail("vova24@test.com");
        user.setPassword("123qw3");

        Optional<User> savedUser = sql2oUser.save(user);

        var rsl = sql2oUser.findByEmailAndPassword(user.getEmail(), user.getPassword());

        assertThat(rsl).isPresent();
        assertThat(rsl.get().getFullName()).isEqualTo(user.getFullName());
        assertThat(rsl.get().getEmail()).isEqualTo(user.getEmail());
        assertThat(rsl.get().getPassword()).isEqualTo(user.getPassword());
        assertThat(rsl.get().getId()).isEqualTo(user.getId());
    }
}