package ru.cinema.repository.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;
import ru.cinema.model.User;

import java.sql.SQLException;
import java.util.Optional;

@Repository
public class Sql2oUserRepository implements UserRepository {

    private static final String UNIQUE_CODE_USER = "23505";
    private static final Logger LOGGER = LoggerFactory.getLogger(Sql2oUserRepository.class);
    private final Sql2o sql2o;

    public Sql2oUserRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<User> save(User user) {
        try (Connection con = sql2o.open()) {
            var query = con.createQuery("""
                            INSERT INTO users (full_name, email, password)
                            VALUES (:fullName, :email, :password)
                            """, true)
                    .addParameter("fullName", user.getFullName())
                    .addParameter("email", user.getEmail())
                    .addParameter("password", user.getPassword());
            int id = query.executeUpdate().getKey(Integer.class);
            user.setId(id);
            return Optional.of(user);
        } catch (Sql2oException e) {
            if (e.getCause() instanceof SQLException sqlException
                    && UNIQUE_CODE_USER.equals(sqlException.getSQLState())) {
                LOGGER.warn("User with email {} already exists", user.getEmail());
            } else {
                LOGGER.error("Error while saving user", e);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        try (Connection con = sql2o.open()) {
            var query = con.createQuery("""
                            SELECT id, full_name AS fullName, email, password
                            FROM users
                            WHERE email = :email AND password = :password
                            """)
                    .addParameter("email", email)
                    .addParameter("password", password);
            var user = query.executeAndFetchFirst(User.class);
            return Optional.ofNullable(user);
        } catch (Sql2oException e) {
            LOGGER.error(
                    "Error while fetching user with email {} and password {}", email, password, e);
        }
        return Optional.empty();
    }
}
