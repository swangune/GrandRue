package mainstreet.infrastructure.persistence.workforce;

import mainstreet.application.MerchantScope;
import mainstreet.workforce.MerchantMembership;
import org.flywaydb.core.Flyway;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JooqMerchantMembershipAuthorityIT {

    private static final MerchantScope MERCHANT_A = new MerchantScope("merchant-a");
    private static final MerchantScope MERCHANT_B = new MerchantScope("merchant-b");
    private static final Instant T0 = Instant.parse("2026-08-24T00:00:00Z");
    private static final Instant T1 = Instant.parse("2026-08-25T00:00:00Z");

    private DSLContext dsl;
    private JooqMerchantWorkforceStore store;
    private JooqMerchantWorkforceAuthority authority;

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
        TransactionAwareDataSourceProxy transactionAware =
                new TransactionAwareDataSourceProxy(dataSource);
        dsl = DSL.using(transactionAware, SQLDialect.POSTGRES);
        store = new JooqMerchantWorkforceStore(
                dsl,
                new DataSourceTransactionManager(dataSource)
        );
        authority = new JooqMerchantWorkforceAuthority(dsl);

        clearMemberships();
        dsl.insertInto(
                        DSL.table(DSL.name("merchant_account")),
                        DSL.field(DSL.name("merchant_identifier"))
                )
                .values(MERCHANT_A.merchantIdentifier())
                .values(MERCHANT_B.merchantIdentifier())
                .onConflictDoNothing()
                .execute();
    }

    @AfterEach
    void tearDown() {
        clearMemberships();
    }

    @Test
    void active_membership_is_current_and_strictly_merchant_scoped() {
        store.establishMembership(MerchantMembership.establish(
                "membership-a",
                MERCHANT_A,
                "identity-1",
                T0
        ));

        assertTrue(authority.isActive(MERCHANT_A, "identity-1"));
        assertFalse(authority.isActive(MERCHANT_B, "identity-1"));
        assertFalse(authority.isActive(MERCHANT_A, "identity-2"));
    }

    @Test
    void suspended_and_ended_membership_are_not_current_authority() {
        store.establishMembership(MerchantMembership.establish(
                "membership-a",
                MERCHANT_A,
                "identity-1",
                T0
        ));

        assertTrue(authority.isActive(MERCHANT_A, "identity-1"));
        assertTrue(store.suspendMembership("membership-a"));
        assertFalse(authority.isActive(MERCHANT_A, "identity-1"));

        assertTrue(store.resumeMembership("membership-a"));
        assertTrue(authority.isActive(MERCHANT_A, "identity-1"));

        assertTrue(store.endMembership("membership-a", T1));
        assertFalse(authority.isActive(MERCHANT_A, "identity-1"));
    }

    private void clearMemberships() {
        dsl.deleteFrom(DSL.table(DSL.name("workforce_role_assignment"))).execute();
        dsl.deleteFrom(DSL.table(DSL.name("workforce_group_membership"))).execute();
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
