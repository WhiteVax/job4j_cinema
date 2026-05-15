package ru.cinema.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import ru.cinema.model.File;

import java.util.Optional;

@Repository
public class Sql2oFileRepository implements FileRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(Sql2oFileRepository.class);
    private final Sql2o sql2o;

    public Sql2oFileRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public void save(File file) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("""
                    INSERT INTO files(name, path) VALUES (:name, :path)
                    """, true).bind(file);
            int generatedId = query.executeUpdate().getKey(Integer.class);
            file.setId(generatedId);
        } catch (Exception e) {
            LOGGER.error("Error while saving file", e);
        }
    }

    @Override
    public Optional<File> findById(int id) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("""
                            SELECT * FROM files WHERE id = :id
                            """)
                    .addParameter("id", id);
            return Optional.ofNullable(query.executeAndFetchFirst(File.class));
        } catch (Exception ex) {
            LOGGER.error("Exception: ", ex);
            return Optional.empty();
        }
    }
}
