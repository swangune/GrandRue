package mainstreet.infrastructure.persistence.notification;

import mainstreet.notification.DeliveryAttempt;
import mainstreet.notification.DeliveryAttemptOutcome;
import mainstreet.notification.DeliveryEvidence;
import mainstreet.notification.NotificationChannel;
import mainstreet.notification.NotificationDispatch;
import mainstreet.notification.NotificationIntent;
import mainstreet.notification.NotificationOwnerScope;
import mainstreet.notification.NotificationRecipient;
import mainstreet.notification.NotificationRecipientResolutionBasis;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JooqNotificationStoreIT {

    private static final NotificationOwnerScope MERCHANT_A =
            NotificationOwnerScope.merchant("merchant-notify-a");
    private static final NotificationOwnerScope MERCHANT_B =
            NotificationOwnerScope.merchant("merchant-notify-b");
    private static final Instant T0 = Instant.parse("2026-08-24T07:00:00Z");

    private DataSource dataSource;
    private DSLContext dsl;
    private JooqNotificationStore store;

    @BeforeEach
    void setUp() {
        dataSource = new DriverManagerDataSource(
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
        store = new JooqNotificationStore(
                dsl,
                new DataSourceTransactionManager(dataSource)
        );
        clearTables();
    }

    @AfterEach
    void tearDown() {
        clearTables();
    }

    @Test
    void duplicate_logical_intent_replays_without_multiplication() {
        NotificationIntent intent = intent("intent-1", MERCHANT_A, "appointment-42");

        assertEquals(intent, store.establishIntent(intent));
        assertEquals(intent, store.establishIntent(intent));
        assertEquals(1, count("notification_intent"));

        NotificationIntent changed = intent("intent-1", MERCHANT_A, "appointment-99");
        assertThrows(
                IllegalArgumentException.class,
                () -> store.establishIntent(changed)
        );
    }

    @Test
    void dispatch_must_preserve_intent_owner_scope() {
        store.establishIntent(intent("intent-1", MERCHANT_A, "appointment-42"));

        assertThrows(
                IllegalArgumentException.class,
                () -> store.establishDispatch(dispatch(
                        "dispatch-1",
                        "intent-1",
                        MERCHANT_B
                ))
        );
        assertEquals(0, count("notification_dispatch"));
    }

    @Test
    void started_attempt_survives_store_recreation_as_unresolved_execution() {
        prepareDispatch();
        DeliveryAttempt started = attempt(
                "attempt-1",
                DeliveryAttemptOutcome.STARTED,
                Optional.empty()
        );
        store.startAttempt(started);

        JooqNotificationStore recreated = new JooqNotificationStore(
                dsl,
                new DataSourceTransactionManager(dataSource)
        );

        assertEquals(started, recreated.findAttempt("attempt-1").orElseThrow());
        assertEquals(DeliveryAttemptOutcome.STARTED,
                recreated.findAttempt("attempt-1").orElseThrow().outcome());
    }

    @Test
    void attempt_can_move_once_from_started_to_terminal_certainty() {
        prepareDispatch();
        store.startAttempt(attempt(
                "attempt-1",
                DeliveryAttemptOutcome.STARTED,
                Optional.empty()
        ));
        DeliveryAttempt uncertain = attempt(
                "attempt-1",
                DeliveryAttemptOutcome.EXECUTION_UNCERTAIN,
                Optional.of("provider-timeout-1")
        );

        assertEquals(uncertain, store.completeAttempt(uncertain));
        assertEquals(uncertain, store.completeAttempt(uncertain));
        assertThrows(
                IllegalArgumentException.class,
                () -> store.completeAttempt(attempt(
                        "attempt-1",
                        DeliveryAttemptOutcome.KNOWN_REJECTED,
                        Optional.of("different-outcome")
                ))
        );
    }

    @Test
    void evidence_is_idempotent_and_cannot_attach_attempt_from_another_dispatch() {
        store.establishIntent(intent("intent-1", MERCHANT_A, "appointment-42"));
        store.establishDispatch(dispatch("dispatch-1", "intent-1", MERCHANT_A));
        store.establishDispatch(dispatch("dispatch-2", "intent-1", MERCHANT_A));
        store.startAttempt(new DeliveryAttempt(
                "attempt-1",
                "dispatch-1",
                MERCHANT_A,
                T0,
                "email-provider-connection-1",
                "dispatch-1",
                DeliveryAttemptOutcome.STARTED,
                Optional.empty()
        ));

        DeliveryEvidence evidence = new DeliveryEvidence(
                "evidence-1",
                "dispatch-1",
                MERCHANT_A,
                Optional.of("attempt-1"),
                "PROVIDER_ACCEPTED",
                T0.plusSeconds(1),
                Optional.of("provider-message-1"),
                "provider-callback-1"
        );
        assertEquals(evidence, store.recordEvidence(evidence));
        assertEquals(evidence, store.recordEvidence(evidence));
        assertEquals(1, count("notification_delivery_evidence"));

        assertThrows(
                IllegalArgumentException.class,
                () -> store.recordEvidence(new DeliveryEvidence(
                        "evidence-2",
                        "dispatch-2",
                        MERCHANT_A,
                        Optional.of("attempt-1"),
                        "PROVIDER_DELIVERED",
                        T0.plusSeconds(2),
                        Optional.of("provider-message-1"),
                        "provider-callback-2"
                ))
        );
    }

    @Test
    void attempt_and_dispatch_state_remain_owner_scope_isolated() {
        store.establishIntent(intent("intent-a", MERCHANT_A, "appointment-a"));
        store.establishIntent(intent("intent-b", MERCHANT_B, "appointment-b"));
        store.establishDispatch(dispatch("dispatch-a", "intent-a", MERCHANT_A));
        store.establishDispatch(dispatch("dispatch-b", "intent-b", MERCHANT_B));

        assertThrows(
                IllegalArgumentException.class,
                () -> store.startAttempt(new DeliveryAttempt(
                        "attempt-cross",
                        "dispatch-a",
                        MERCHANT_B,
                        T0,
                        "provider-context",
                        "dispatch-a",
                        DeliveryAttemptOutcome.STARTED,
                        Optional.empty()
                ))
        );
        assertTrue(store.attemptsForDispatch("dispatch-a").isEmpty());
    }

    private void prepareDispatch() {
        store.establishIntent(intent("intent-1", MERCHANT_A, "appointment-42"));
        store.establishDispatch(dispatch("dispatch-1", "intent-1", MERCHANT_A));
    }

    private static NotificationIntent intent(
            String identity,
            NotificationOwnerScope scope,
            String sourceReference
    ) {
        return new NotificationIntent(
                identity,
                "appointment.confirmed",
                scope,
                sourceReference,
                "commitment-contact-for-appointment",
                Optional.of("correlation-1"),
                Optional.of("appointment-confirmed-event-1"),
                T0
        );
    }

    private static NotificationDispatch dispatch(
            String identity,
            String intentIdentity,
            NotificationOwnerScope scope
    ) {
        return new NotificationDispatch(
                identity,
                intentIdentity,
                scope,
                new NotificationRecipient(
                        "recipient-1",
                        "CustomerContext",
                        "customer-context-42",
                        NotificationRecipientResolutionBasis.COMMITMENT_BOUND
                ),
                NotificationChannel.EMAIL,
                "contact-endpoint-reference-1",
                "rendered-content-reference-1",
                T0
        );
    }

    private static DeliveryAttempt attempt(
            String identity,
            DeliveryAttemptOutcome outcome,
            Optional<String> failure
    ) {
        return new DeliveryAttempt(
                identity,
                "dispatch-1",
                MERCHANT_A,
                T0,
                "email-provider-connection-1",
                "dispatch-1",
                outcome,
                failure
        );
    }

    private int count(String table) {
        return dsl.fetchCount(DSL.table(DSL.name(table)));
    }

    private void clearTables() {
        dsl.deleteFrom(DSL.table(DSL.name("notification_delivery_evidence"))).execute();
        dsl.deleteFrom(DSL.table(DSL.name("notification_delivery_attempt"))).execute();
        dsl.deleteFrom(DSL.table(DSL.name("notification_dispatch"))).execute();
        dsl.deleteFrom(DSL.table(DSL.name("notification_intent"))).execute();
    }

    private static String requiredEnvironment(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required environment variable: " + name);
        }
        return value;
    }
}
