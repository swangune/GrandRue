package grandrue.infrastructure.persistence;

import org.flywaydb.core.Flyway;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PostgresPersistenceFoundationIT {

    private static final String PROBE_SCHEMA = "persistence_foundation_it";

    @Test
    void postgres18_flyway_jooq_and_spring_transactions_form_one_persistence_foundation() {
        DataSource authoritativeDataSource = new DriverManagerDataSource(
                requiredEnvironment("GRANDRUE_TEST_POSTGRES_URL"),
                requiredEnvironment("GRANDRUE_TEST_POSTGRES_USER"),
                requiredEnvironment("GRANDRUE_TEST_POSTGRES_PASSWORD")
        );

        Flyway flyway = Flyway.configure()
                .dataSource(authoritativeDataSource)
                .locations("classpath:db/persistence-foundation")
                .schemas(PROBE_SCHEMA)
                .defaultSchema(PROBE_SCHEMA)
                .load();

        flyway.migrate();

        TransactionAwareDataSourceProxy transactionAwareDataSource =
                new TransactionAwareDataSourceProxy(authoritativeDataSource);
        DSLContext dsl = DSL.using(transactionAwareDataSource, SQLDialect.POSTGRES);

        Integer postgresMajorVersion = dsl
                .fetchOne("select current_setting('server_version_num')::integer / 10000")
                .get(0, Integer.class);
        assertEquals(18, postgresMajorVersion);

        Boolean flywayCreatedProbeTable = dsl
                .fetchOne(
                        "select to_regclass(?) is not null",
                        PROBE_SCHEMA + ".persistence_foundation_probe"
                )
                .get(0, Boolean.class);
        assertTrue(flywayCreatedProbeTable);

        var probeTable = DSL.table(DSL.name(PROBE_SCHEMA, "persistence_foundation_probe"));
        var probeId = DSL.field(DSL.name("probe_id"));

        dsl.deleteFrom(probeTable).execute();

        DataSourceTransactionManager transactionManager =
                new DataSourceTransactionManager(authoritativeDataSource);
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);

        transactionTemplate.executeWithoutResult(status -> {
            dsl.insertInto(probeTable, probeId)
                    .values("rollback-probe")
                    .execute();
            status.setRollbackOnly();
        });

        assertEquals(0, dsl.fetchCount(probeTable));
    }

    private static String requiredEnvironment(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Missing required PostgreSQL integration-test environment variable: " + name
            );
        }
        return value;
    }
}
