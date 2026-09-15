package mainstreet.infrastructure.persistence.protection;

import mainstreet.protection.ProtectionAdmissionDecision;
import mainstreet.protection.ProtectionPolicy;
import mainstreet.protection.ProtectionStateFailureBehaviour;
import mainstreet.protection.ProtectionSubject;
import mainstreet.protection.ProtectionTarget;
import mainstreet.protection.TemporaryProtectiveRestriction;
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
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JooqResourceProtectionAuthorityIT {

    private static final ProtectionTarget TARGET =
            new ProtectionTarget("notification-dispatch");
    private static final ProtectionSubject MERCHANT_A =
            new ProtectionSubject("MERCHANT", "merchant-protect-a");
    private static final ProtectionSubject MERCHANT_B =
            new ProtectionSubject("MERCHANT", "merchant-protect-b");
    private static final Instant T0 = Instant.parse("2026-08-24T05:30:00Z");

    private DataSource dataSource;
    private DSLContext dsl;
    private MutableClock clock;
    private JooqResourceProtectionAuthority authority;

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
        clock = new MutableClock(T0);
        authority = new JooqResourceProtectionAuthority(
                dsl,
                new DataSourceTransactionManager(dataSource),
                clock,
                Set.of(TARGET)
        );
        clearTables();
    }

    @AfterEach
    void tearDown() {
        clearTables();
    }

    @Test
    void admitted_logical_consumption_is_counted_once_and_exhaustion_rejects() {
        ProtectionPolicy policy = policy(2, ProtectionAdmissionDecision.REJECT);

        assertEquals(
                ProtectionAdmissionDecision.ADMIT,
                authority.admit(policy, MERCHANT_A, "logical-1", 1, false)
        );
        assertEquals(
                ProtectionAdmissionDecision.ADMIT,
                authority.admit(policy, MERCHANT_A, "logical-1", 1, false)
        );
        assertEquals(
                ProtectionAdmissionDecision.ADMIT,
                authority.admit(policy, MERCHANT_A, "logical-2", 1, false)
        );
        assertEquals(
                ProtectionAdmissionDecision.REJECT,
                authority.admit(policy, MERCHANT_A, "logical-3", 1, false)
        );

        assertEquals(2L, consumedUnits());
        assertEquals(2, count("resource_protection_consumption_evidence"));
    }

    @Test
    void concurrent_candidates_cannot_overconsume_one_remaining_unit() throws Exception {
        ProtectionPolicy policy = policy(1, ProtectionAdmissionDecision.REJECT);
        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);

        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            Future<ProtectionAdmissionDecision> first = executor.submit(() -> {
                ready.countDown();
                start.await();
                return authority.admit(policy, MERCHANT_A, "race-1", 1, false);
            });
            Future<ProtectionAdmissionDecision> second = executor.submit(() -> {
                ready.countDown();
                start.await();
                return authority.admit(policy, MERCHANT_A, "race-2", 1, false);
            });
            ready.await();
            start.countDown();

            List<ProtectionAdmissionDecision> results = List.of(first.get(), second.get());
            assertEquals(1, results.stream()
                    .filter(ProtectionAdmissionDecision.ADMIT::equals)
                    .count());
            assertEquals(1, results.stream()
                    .filter(ProtectionAdmissionDecision.REJECT::equals)
                    .count());
        }

        assertEquals(1L, consumedUnits());
        assertEquals(1, count("resource_protection_consumption_evidence"));
    }

    @Test
    void defer_is_returned_only_when_policy_and_owning_execution_both_allow_it() {
        ProtectionPolicy policy = policy(1, ProtectionAdmissionDecision.DEFER);
        assertEquals(
                ProtectionAdmissionDecision.ADMIT,
                authority.admit(policy, MERCHANT_A, "first", 1, true)
        );
        assertEquals(
                ProtectionAdmissionDecision.REJECT,
                authority.admit(policy, MERCHANT_A, "second", 1, false)
        );
        assertEquals(
                ProtectionAdmissionDecision.DEFER,
                authority.admit(policy, MERCHANT_A, "second", 1, true)
        );
    }

    @Test
    void bounded_restriction_rejects_only_matching_subject_and_target_until_expiry() {
        authority.establish(new TemporaryProtectiveRestriction(
                "restriction-1",
                MERCHANT_A,
                Set.of(TARGET),
                T0,
                T0.plusSeconds(60),
                "resource-exhaustion-pattern"
        ));

        ProtectionPolicy policy = policy(5, ProtectionAdmissionDecision.REJECT);
        assertEquals(
                ProtectionAdmissionDecision.REJECT,
                authority.admit(policy, MERCHANT_A, "a-1", 1, false)
        );
        assertEquals(
                ProtectionAdmissionDecision.ADMIT,
                authority.admit(policy, MERCHANT_B, "b-1", 1, false)
        );

        clock.set(T0.plusSeconds(60));
        assertEquals(
                ProtectionAdmissionDecision.ADMIT,
                authority.admit(policy, MERCHANT_A, "a-2", 1, false)
        );
    }

    @Test
    void subject_budgets_are_independent() {
        ProtectionPolicy policy = policy(1, ProtectionAdmissionDecision.REJECT);
        assertEquals(
                ProtectionAdmissionDecision.ADMIT,
                authority.admit(policy, MERCHANT_A, "a-1", 1, false)
        );
        assertEquals(
                ProtectionAdmissionDecision.REJECT,
                authority.admit(policy, MERCHANT_A, "a-2", 1, false)
        );
        assertEquals(
                ProtectionAdmissionDecision.ADMIT,
                authority.admit(policy, MERCHANT_B, "b-1", 1, false)
        );
    }

    @Test
    void a_new_time_window_has_fresh_capacity_without_erasing_old_evidence() {
        ProtectionPolicy policy = policy(1, ProtectionAdmissionDecision.REJECT);
        assertEquals(
                ProtectionAdmissionDecision.ADMIT,
                authority.admit(policy, MERCHANT_A, "window-1", 1, false)
        );
        assertEquals(
                ProtectionAdmissionDecision.REJECT,
                authority.admit(policy, MERCHANT_A, "window-2", 1, false)
        );

        clock.set(T0.plusSeconds(60));
        assertEquals(
                ProtectionAdmissionDecision.ADMIT,
                authority.admit(policy, MERCHANT_A, "window-2", 1, false)
        );
        assertEquals(2, count("resource_protection_consumption_window"));
        assertEquals(2, count("resource_protection_consumption_evidence"));
    }

    @Test
    void unregistered_target_is_rejected_before_state_mutation() {
        ProtectionPolicy policy = new ProtectionPolicy(
                "unregistered-policy",
                1,
                new ProtectionTarget("unregistered-target"),
                "MERCHANT",
                "LOGICAL_APPLICATION_REQUEST",
                1,
                Duration.ofMinutes(1),
                ProtectionAdmissionDecision.REJECT,
                ProtectionStateFailureBehaviour.REJECT
        );

        boolean rejected = false;
        try {
            authority.admit(policy, MERCHANT_A, "x", 1, false);
        } catch (IllegalArgumentException expected) {
            rejected = true;
        }
        assertTrue(rejected);
        assertEquals(0, count("resource_protection_consumption_window"));
    }

    private ProtectionPolicy policy(
            long capacity,
            ProtectionAdmissionDecision exhaustion
    ) {
        return new ProtectionPolicy(
                "notification-dispatch-per-merchant",
                1,
                TARGET,
                "MERCHANT",
                "LOGICAL_APPLICATION_REQUEST",
                capacity,
                Duration.ofMinutes(1),
                exhaustion,
                ProtectionStateFailureBehaviour.REJECT
        );
    }

    private long consumedUnits() {
        Long value = dsl.select(DSL.sum(
                        DSL.field(DSL.name("consumed_units"), Long.class)
                ))
                .from(DSL.table(DSL.name("resource_protection_consumption_window")))
                .fetchOne(0, Long.class);
        return value == null ? 0L : value;
    }

    private int count(String table) {
        return dsl.fetchCount(DSL.table(DSL.name(table)));
    }

    private void clearTables() {
        dsl.deleteFrom(DSL.table(DSL.name("temporary_protective_restriction_target")))
                .execute();
        dsl.deleteFrom(DSL.table(DSL.name("temporary_protective_restriction")))
                .execute();
        dsl.deleteFrom(DSL.table(DSL.name("resource_protection_consumption_evidence")))
                .execute();
        dsl.deleteFrom(DSL.table(DSL.name("resource_protection_consumption_window")))
                .execute();
    }

    private static String requiredEnvironment(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required environment variable: " + name);
        }
        return value;
    }

    private static final class MutableClock extends Clock {
        private Instant instant;

        private MutableClock(Instant instant) {
            this.instant = instant;
        }

        private void set(Instant newInstant) {
            this.instant = newInstant;
        }

        @Override
        public ZoneId getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(ZoneId zone) {
            return Clock.fixed(instant, zone);
        }

        @Override
        public Instant instant() {
            return instant;
        }
    }
}
