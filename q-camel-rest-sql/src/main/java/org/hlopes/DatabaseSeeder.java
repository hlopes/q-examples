package org.hlopes;

import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@Startup
@ApplicationScoped
public class DatabaseSeeder {

    @Inject
    DataSource dataSource;

    @PostConstruct
    void setupDb() {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();

            statement.execute("DROP TABLE IF EXISTS users;");
            statement.execute("""
                    CREATE TABLE users (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        city VARCHAR(255)
                    );
                    """);
            statement.execute("""
                    INSERT INTO users (name, city) VALUES
                        ('Alice', 'Amesterdam'),
                        ('Bob', 'Berlin'),
                        ('Carol', 'Copenhagen');
                    """);
        } catch (Exception e) {
            throw new RuntimeException("Could not initialize database", e);
        }
    }
}
