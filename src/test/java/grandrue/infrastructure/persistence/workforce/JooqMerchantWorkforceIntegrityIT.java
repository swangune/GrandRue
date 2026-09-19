package grandrue.infrastructure.persistence.workforce;

import grandrue.application.MerchantScope;
import grandrue.workforce.MerchantAccessGroup;
import grandrue.workforce.MerchantGroupMembership;
import grandrue.workforce.MerchantMembership;
import org.flywaydb.core.Flyway;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JooqMerchantWorkforceIntegrityIT {

    private static final MerchantScope SCOPE =
            new MerchantScope("merchant-workforce-integrity");
    private static final Instant T0 = Instant.parse("2026-08-24T00:00:00Z");
    private static final Instant T1 = Instant.parse("2026-08-25T00:00:00Z");

    private DSLContext dsl;
    private JooqMerchantWorkforceStore store;

    @BeforeEach
    void setUp() {
        DataSource dataSource = new DriverManagerDataSource(
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_URL"),
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_USER"),
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_PASSWORD")
        );
        Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .load()
                .migrate();

        dsl = DSL.using(
                new TransactionAwareDataSourceProxy(dataSource),
                SQLDialect.POSTGRES
        );
        store = new JooqMerchantWorkforceStore(
                dsl,
                new DataSourceTransactionManager(dataSource)
        );

        clearWorkforceTables();
        dsl.insertInto(
                        DSL.table(DSL.name("merchant_account")),
                        DSL.field(DSL.name("merchant_identifier"))
                )
                .values(SCOPE.merchantIdentifier())
                .onConflictDoNothing()
                .execute();
    }

    @AfterEach
    void tearDown() {
        clearWorkforceTables();
    }

    @Test
    void one_identity_has_at_most_one_current_membership_but_may_rejoin_after_end() {
        MerchantMembership first = MerchantMembership.establish(
                "membership-first",
                SCOPE,
                "identity-1",
                T0
        );
        MerchantMembership duplicateCurrent = MerchantMembership.establish(
                "membership-duplicate",
                SCOPE,
                "identity-1",
                T0
        );

        store.establishMembership(first);
        assertThrows(
                DataAccessException.class,
                () -> store.establishMembership(duplicateCurrent)
        );

        assertTrue(store.endMembership("membership-first", T1));
        MerchantMembership rejoined = MerchantMembership.establish(
                "membership-rejoined",
                SCOPE,
                "identity-1",
                T1
        );
        store.establishMembership(rejoined);

        assertEquals(
                2,
                dsl.fetchCount(DSL.table(DSL.name("workforce_merchant_membership")))
        );
    }

    @Test
    void group_membership_revalidates_current_membership_in_database() {
        MerchantMembership membership = MerchantMembership.establish(
                "membership-1",
                SCOPE,
                "identity-1",
                T0
        );
        MerchantAccessGroup group = new MerchantAccessGroup(
                "group-1",
                SCOPE,
                "Managers"
        );
        MerchantGroupMembership staleCandidate = MerchantGroupMembership.establish(
                "group-membership-1",
                membership,
                group
        );

        store.establishMembership(membership);
        store.createAccessGroup(group);
        assertTrue(store.suspendMembership("membership-1"));

        assertThrows(
                IllegalStateException.class,
                () -> store.establishGroupMembership(staleCandidate)
        );
        assertEquals(
                0,
                dsl.fetchCount(DSL.table(DSL.name("workforce_group_membership")))
        );
    }

    private void clearWorkforceTables() {
        dsl.deleteFrom(DSL.table(DSL.name("workforce_role_assignment"))).execute();
        dsl.deleteFrom(DSL.table(DSL.name("workforce_role_privilege"))).execute();
        dsl.deleteFrom(DSL.table(DSL.name("workforce_role_definition"))).execute();
        dsl.deleteFrom(DSL.table(DSL.name("workforce_group_membership"))).execute();
        dsl.deleteFrom(DSL.table(DSL.name("workforce_access_group"))).execute();
        dsl.deleteFrom(DSL.table(DSL.name("workforce_merchant_membership"))).execute();
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
