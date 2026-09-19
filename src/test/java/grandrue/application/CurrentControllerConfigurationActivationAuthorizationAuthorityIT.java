package grandrue.application;

import grandrue.infrastructure.persistence.merchantaccount
        .JooqMerchantAccountLifecycleStore;
import grandrue.merchantaccount.MerchantAccountLifecycle;
import grandrue.merchantaccount.MerchantAccountLifecycleStore;
import org.flywaydb.core.Flyway;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CurrentControllerConfigurationActivationAuthorizationAuthorityIT {

    private static final String MERCHANT =
            "merchant-r3-activation-authority";

    private static final String CONTROLLER =
            "controller-r3-a";

    private static final Instant NOW =
            Instant.parse("2026-09-11T19:30:00Z");

    private DSLContext dsl;

    private CurrentControllerConfigurationActivationAuthorizationAuthority
            authority;

    @BeforeEach
    void setUp() {
        DataSource source =
                new DriverManagerDataSource(
                        required(
                                "MAINSTREET_TEST_POSTGRES_URL"
                        ),
                        required(
                                "MAINSTREET_TEST_POSTGRES_USER"
                        ),
                        required(
                                "MAINSTREET_TEST_POSTGRES_PASSWORD"
                        )
                );

        DataSourceTransactionManager transactions =
                new DataSourceTransactionManager(source);

        Flyway.configure()
                .dataSource(source)
                .locations("classpath:db/migration")
                .load()
                .migrate();

        dsl = DSL.using(
                new TransactionAwareDataSourceProxy(source),
                SQLDialect.POSTGRES
        );

        clearMerchant();

        dsl.execute(
                "insert into merchant_account "
                        + "(merchant_identifier, lifecycle) "
                        + "values (?, 'OPEN')",
                MERCHANT
        );

        dsl.execute(
                "insert into merchant_controller_relationship "
                        + "(controller_relationship_identifier, "
                        + "merchant_identifier, "
                        + "identity_identifier, "
                        + "lifecycle) "
                        + "values (?, ?, ?, 'ACTIVE')",
                "controller-rel-r3-a",
                MERCHANT,
                CONTROLLER
        );

        MerchantAccountLifecycleStore accounts =
                new JooqMerchantAccountLifecycleStore(
                        dsl,
                        transactions
                );

        authority =
                new CurrentControllerConfigurationActivationAuthorizationAuthority(
                        accounts
                );
    }

    @Test
    void open_unsuspended_current_controller_is_authorized() {
        assertTrue(
                authority.isAuthorized(
                        CONTROLLER,
                        MERCHANT,
                        "configuration-2"
                )
        );
    }

    @Test
    void different_identity_is_not_authorized() {
        assertFalse(
                authority.isAuthorized(
                        "staff-r3",
                        MERCHANT,
                        "configuration-2"
                )
        );
    }

    @Test
    void closing_merchant_account_is_not_authorized() {
        dsl.execute(
                "update merchant_account "
                        + "set lifecycle = 'CLOSING' "
                        + "where merchant_identifier = ?",
                MERCHANT
        );

        assertFalse(
                authority.isAuthorized(
                        CONTROLLER,
                        MERCHANT,
                        "configuration-2"
                )
        );
    }

    @Test
    void closed_merchant_account_is_not_authorized() {
        dsl.execute(
                "update merchant_controller_relationship "
                        + "set lifecycle = 'ENDED' "
                        + "where merchant_identifier = ? "
                        + "and lifecycle = 'ACTIVE'",
                MERCHANT
        );

        dsl.execute(
                "update merchant_account "
                        + "set lifecycle = 'CLOSED' "
                        + "where merchant_identifier = ?",
                MERCHANT
        );

        assertFalse(
                authority.isAuthorized(
                        CONTROLLER,
                        MERCHANT,
                        "configuration-2"
                )
        );
    }

    @Test
    void effective_account_wide_suspension_blocks_activation_authority() {
        dsl.execute(
                "insert into merchant_account_suspension "
                        + "(suspension_identity, "
                        + "merchant_identifier, "
                        + "source_authority_identifier, "
                        + "reason_class_identifier, "
                        + "established_at, "
                        + "established_by_identifier, "
                        + "release_authority_identifier, "
                        + "provenance_identifier) "
                        + "values (?, ?, ?, ?, "
                        + "cast(? as timestamptz), ?, ?, ?)",
                "suspension-r3-1",
                MERCHANT,
                "risk-authority",
                "ACCOUNT_RISK",
                NOW.toString(),
                "platform-risk",
                "risk-authority",
                "risk-case-r3-1"
        );

        assertFalse(
                authority.isAuthorized(
                        CONTROLLER,
                        MERCHANT,
                        "configuration-2"
                )
        );
    }

    @Test
    void released_suspension_no_longer_blocks_activation_authority() {
        dsl.execute(
                "insert into merchant_account_suspension "
                        + "(suspension_identity, "
                        + "merchant_identifier, "
                        + "source_authority_identifier, "
                        + "reason_class_identifier, "
                        + "established_at, "
                        + "established_by_identifier, "
                        + "release_authority_identifier, "
                        + "provenance_identifier, "
                        + "released_at, "
                        + "released_by_identifier, "
                        + "release_evidence_identifier) "
                        + "values (?, ?, ?, ?, "
                        + "cast(? as timestamptz), ?, ?, ?, "
                        + "cast(? as timestamptz), ?, ?)",
                "suspension-r3-released",
                MERCHANT,
                "risk-authority",
                "ACCOUNT_RISK",
                NOW.minusSeconds(120).toString(),
                "platform-risk",
                "risk-authority",
                "risk-case-r3-released",
                NOW.minusSeconds(60).toString(),
                "platform-risk",
                "release-evidence-r3"
        );

        assertTrue(
                authority.isAuthorized(
                        CONTROLLER,
                        MERCHANT,
                        "configuration-2"
                )
        );
    }

    @Test
    void ended_controller_relationship_does_not_authorize_old_controller() {
        dsl.execute(
                "update merchant_controller_relationship "
                        + "set lifecycle = 'ENDED' "
                        + "where merchant_identifier = ? "
                        + "and lifecycle = 'ACTIVE'",
                MERCHANT
        );

        dsl.execute(
                "insert into merchant_controller_relationship "
                        + "(controller_relationship_identifier, "
                        + "merchant_identifier, "
                        + "identity_identifier, "
                        + "lifecycle) "
                        + "values (?, ?, ?, 'ACTIVE')",
                "controller-rel-r3-b",
                MERCHANT,
                "controller-r3-b"
        );

        assertFalse(
                authority.isAuthorized(
                        CONTROLLER,
                        MERCHANT,
                        "configuration-2"
                )
        );

        assertTrue(
                authority.isAuthorized(
                        "controller-r3-b",
                        MERCHANT,
                        "configuration-2"
                )
        );
    }

    private void clearMerchant() {
        dsl.execute(
                "delete from merchant_account_suspension "
                        + "where merchant_identifier = ?",
                MERCHANT
        );

        dsl.execute(
                "delete from merchant_controller_relationship "
                        + "where merchant_identifier = ?",
                MERCHANT
        );

        dsl.execute(
                "delete from merchant_account "
                        + "where merchant_identifier = ?",
                MERCHANT
        );
    }

    private static String required(String name) {
        String value = System.getenv(name);

        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Missing " + name
            );
        }

        return value;
    }
}