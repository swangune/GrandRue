package grandrue.infrastructure.persistence;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * IMP-01 migration harness: proves the complete production Flyway chain can
 * initialise a clean PostgreSQL schema and is stable when migration is retried.
 */
class FlywayMigrationChainIT {

    private static final String SCHEMA = "migration_chain_it";

    private DataSource dataSource;

    @BeforeEach
    void setUp() throws SQLException {
        dataSource = new DriverManagerDataSource(
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_URL"),
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_USER"),
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_PASSWORD")
        );
        execute("drop schema if exists " + SCHEMA + " cascade");
        execute("create schema " + SCHEMA);
    }

    @AfterEach
    void tearDown() throws SQLException {
        execute("drop schema if exists " + SCHEMA + " cascade");
    }

    @Test
    void complete_migration_chain_initialises_clean_schema_and_is_idempotent()
            throws SQLException {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .schemas(SCHEMA)
                .defaultSchema(SCHEMA)
                .load();

        flyway.migrate();
        flyway.validate();

        int appliedAfterInitialMigration = successfulMigrationCount();
        assertTrue(
                appliedAfterInitialMigration > 0,
                "Production migration chain must contain applied migrations"
        );

        flyway.migrate();
        flyway.validate();

        assertEquals(
                appliedAfterInitialMigration,
                successfulMigrationCount(),
                "Re-running Flyway must not create duplicate migration history"
        );
    }

    private int successfulMigrationCount() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(
                     "select count(*) from " + SCHEMA
                             + ".flyway_schema_history where success = true"
             )) {
            result.next();
            return result.getInt(1);
        }
    }

    private void execute(String sql) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    private static String requiredEnvironment(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Missing required PostgreSQL integration-test environment variable: "
                            + name
            );
        }
        return value;
    }
}
