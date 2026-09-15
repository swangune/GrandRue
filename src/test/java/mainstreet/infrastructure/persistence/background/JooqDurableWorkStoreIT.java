package mainstreet.infrastructure.persistence.background;

import mainstreet.application.MerchantScope;
import mainstreet.background.BackgroundExecutionScope;
import mainstreet.background.BackgroundWorkResultClassification;
import mainstreet.background.ClaimedWork;
import mainstreet.background.DurableWorkInstruction;
import mainstreet.background.OverdueHandling;
import mainstreet.background.WorkAttempt;
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
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JooqDurableWorkStoreIT {

    private static final MerchantScope MERCHANT_A =
            new MerchantScope("merchant-a");

    private static final Instant T0 =
            Instant.parse("2026-08-24T15:00:00Z");

    private static final String SCHEDULED_PRINCIPAL =
            "scheduled-booking-principal";

    private DataSource authoritativeDataSource;
    private DSLContext dsl;
    private DataSourceTransactionManager transactionManager;

    @BeforeEach
    void setUp() {
        authoritativeDataSource = new DriverManagerDataSource(
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_URL"),
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_USER"),
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_PASSWORD")
        );

        Flyway.configure()
                .dataSource(authoritativeDataSource)
                .locations("classpath:db/migration")
                .load()
                .migrate();

        dsl = DSL.using(
                new TransactionAwareDataSourceProxy(
                        authoritativeDataSource
                ),
                SQLDialect.POSTGRES
        );

        transactionManager =
                new DataSourceTransactionManager(
                        authoritativeDataSource
                );

        dsl.execute(
                "truncate table durable_work_attempt, durable_work_instruction"
        );

        dsl.execute(
                "insert into merchant_account (merchant_identifier) "
                        + "values ('merchant-a') on conflict do nothing"
        );
    }

    @Test
    void instruction_survives_recreation_and_identity_replay_requires_exact_intent() {
        DurableWorkInstruction original =
                work("work-1", T0);

        assertEquals(
                original,
                store().schedule(original)
        );

        assertEquals(
                original,
                store().instruction("work-1").orElseThrow()
        );

        assertEquals(
                original,
                store().schedule(original)
        );

        DurableWorkInstruction changed =
                new DurableWorkInstruction(
                        original.workIdentity(),
                        original.ownerContextIdentifier(),
                        original.executionScope(),
                        original.merchantScope(),
                        original.dueAt().plusSeconds(1),
                        original.responsibilityIdentifier(),
                        original.correlationIdentifier(),
                        original.causationIdentifier(),
                        original.semanticProvenanceReference(),
                        original.retryPolicyReference(),
                        original.overdueHandling(),
                        original.createdAt()
                );

        assertThrows(
                IllegalStateException.class,
                () -> store().schedule(changed)
        );

        assertEquals(
                1,
                count("durable_work_instruction")
        );
    }

    @Test
    void platform_and_merchant_work_persist_without_fake_scope() {
        DurableWorkInstruction merchant =
                work("work-merchant", T0);

        DurableWorkInstruction platform =
                new DurableWorkInstruction(
                        "work-platform",
                        "platform-security",
                        BackgroundExecutionScope.PLATFORM,
                        Optional.empty(),
                        T0,
                        "security.cleanup.evaluate",
                        "correlation-platform",
                        Optional.empty(),
                        Optional.empty(),
                        "security-cleanup-v1",
                        OverdueHandling.RE_EVALUATE_CURRENT_STATE,
                        T0.minusSeconds(5)
                );

        store().schedule(merchant);
        store().schedule(platform);

        assertEquals(
                MERCHANT_A,
                store().instruction("work-merchant")
                        .orElseThrow()
                        .merchantScope()
                        .orElseThrow()
        );

        assertTrue(
                store().instruction("work-platform")
                        .orElseThrow()
                        .merchantScope()
                        .isEmpty()
        );
    }

    @Test
    void only_due_work_is_claimed_in_due_order_and_future_work_remains_unclaimed() {
        store().schedule(
                work("work-later", T0.plusSeconds(10))
        );

        store().schedule(
                work("work-first", T0.minusSeconds(10))
        );

        store().schedule(
                work("work-second", T0.minusSeconds(5))
        );

        List<ClaimedWork> claimed =
                store().claimDue(
                        "worker-1",
                        T0,
                        T0.plusSeconds(30),
                        10
                );

        assertEquals(
                List.of(
                        "work-first",
                        "work-second"
                ),
                claimed.stream()
                        .map(item ->
                                item.instruction()
                                        .workIdentity())
                        .toList()
        );

        assertEquals(
                0,
                store().claimDue(
                        "worker-2",
                        T0,
                        T0.plusSeconds(30),
                        10
                ).size()
        );
    }

    @Test
    void concurrent_claimers_never_receive_the_same_due_work()
            throws Exception {

        store().schedule(
                work("work-1", T0)
        );

        CountDownLatch start =
                new CountDownLatch(1);

        try (ExecutorService executor =
                     Executors.newFixedThreadPool(2)) {

            Future<List<ClaimedWork>> first =
                    executor.submit(() -> {
                        start.await();

                        return independentStore().claimDue(
                                "worker-1",
                                T0,
                                T0.plusSeconds(30),
                                1
                        );
                    });

            Future<List<ClaimedWork>> second =
                    executor.submit(() -> {
                        start.await();

                        return independentStore().claimDue(
                                "worker-2",
                                T0,
                                T0.plusSeconds(30),
                                1
                        );
                    });

            start.countDown();

            List<ClaimedWork> firstClaim =
                    first.get();

            List<ClaimedWork> secondClaim =
                    second.get();

            assertEquals(
                    1,
                    firstClaim.size()
                            + secondClaim.size()
            );
        }
    }

    @Test
    void live_lease_blocks_reclaim_but_expired_lease_may_be_reclaimed() {
        store().schedule(
                work("work-1", T0)
        );

        assertEquals(
                1,
                store().claimDue(
                        "worker-1",
                        T0,
                        T0.plusSeconds(60),
                        1
                ).size()
        );

        assertTrue(
                store().claimDue(
                        "worker-2",
                        T0.plusSeconds(59),
                        T0.plusSeconds(90),
                        1
                ).isEmpty()
        );

        List<ClaimedWork> reclaimed =
                store().claimDue(
                        "worker-2",
                        T0.plusSeconds(60),
                        T0.plusSeconds(120),
                        1
                );

        assertEquals(
                1,
                reclaimed.size()
        );

        assertEquals(
                "worker-2",
                reclaimed.getFirst()
                        .workerIdentity()
        );
    }

    @Test
    void attempt_start_is_durable_before_outcome_is_known_and_exact_replay_is_idempotent() {
        JooqDurableWorkStore store =
                store();

        store.schedule(
                work("work-1", T0)
        );

        store.claimDue(
                "worker-1",
                T0,
                T0.plusSeconds(60),
                1
        );

        WorkAttempt started =
                store.startAttempt(
                        "attempt-1",
                        "work-1",
                        "worker-1",
                        T0.plusSeconds(1),
                        SCHEDULED_PRINCIPAL
                );

        assertEquals(
                "attempt-1",
                started.attemptIdentity()
        );

        assertEquals(
                "work-1",
                started.workIdentity()
        );

        assertEquals(
                T0.plusSeconds(1),
                started.attemptedAt()
        );

        assertEquals(
                SCHEDULED_PRINCIPAL,
                started.principalReference()
        );

        assertTrue(
                started.resultClassification()
                        .isEmpty()
        );

        assertTrue(
                started.evidenceReference()
                        .isEmpty()
        );

        assertEquals(
                started,
                independentStore()
                        .latestAttempt("work-1")
                        .orElseThrow()
        );

        assertEquals(
                started,
                store.startAttempt(
                        "attempt-1",
                        "work-1",
                        "worker-1",
                        T0.plusSeconds(1),
                        SCHEDULED_PRINCIPAL
                )
        );

        assertThrows(
                IllegalStateException.class,
                () -> store.startAttempt(
                        "attempt-1",
                        "work-1",
                        "worker-1",
                        T0.plusSeconds(1),
                        "different-principal"
                )
        );

        assertEquals(
                1,
                count("durable_work_attempt")
        );
    }

    @Test
    void expired_lease_reclaim_does_not_authorise_blind_second_attempt_when_prior_outcome_is_unresolved() {
        JooqDurableWorkStore store =
                store();

        store.schedule(
                work("work-1", T0)
        );

        store.claimDue(
                "worker-1",
                T0,
                T0.plusSeconds(60),
                1
        );

        store.startAttempt(
                "attempt-1",
                "work-1",
                "worker-1",
                T0.plusSeconds(1),
                SCHEDULED_PRINCIPAL
        );

        assertEquals(
                1,
                store.claimDue(
                        "worker-2",
                        T0.plusSeconds(60),
                        T0.plusSeconds(120),
                        1
                ).size()
        );

        assertThrows(
                IllegalStateException.class,
                () -> store.startAttempt(
                        "attempt-2",
                        "work-1",
                        "worker-2",
                        T0.plusSeconds(61),
                        SCHEDULED_PRINCIPAL
                )
        );

        WorkAttempt unresolved =
                store.latestAttempt("work-1")
                        .orElseThrow();

        assertEquals(
                "attempt-1",
                unresolved.attemptIdentity()
        );

        assertTrue(
                unresolved.resultClassification()
                        .isEmpty()
        );

        assertEquals(
                1,
                count("durable_work_attempt")
        );
    }

    @Test
    void attempt_outcome_is_durable_idempotent_and_cannot_be_reclassified() {
        JooqDurableWorkStore store =
                store();

        store.schedule(
                work("work-1", T0)
        );

        store.claimDue(
                "worker-1",
                T0,
                T0.plusSeconds(60),
                1
        );

        store.startAttempt(
                "attempt-1",
                "work-1",
                "worker-1",
                T0.plusSeconds(1),
                SCHEDULED_PRINCIPAL
        );

        WorkAttempt classified =
                store.recordAttemptOutcome(
                        "attempt-1",
                        "worker-1",
                        BackgroundWorkResultClassification
                                .RECONCILIATION_REQUIRED,
                        Optional.of(
                                "owner-outcome-unresolved"
                        )
                );

        assertEquals(
                Optional.of(
                        BackgroundWorkResultClassification
                                .RECONCILIATION_REQUIRED
                ),
                classified.resultClassification()
        );

        assertEquals(
                Optional.of(
                        "owner-outcome-unresolved"
                ),
                classified.evidenceReference()
        );

        assertEquals(
                classified,
                independentStore()
                        .latestAttempt("work-1")
                        .orElseThrow()
        );

        assertEquals(
                classified,
                store.recordAttemptOutcome(
                        "attempt-1",
                        "worker-1",
                        BackgroundWorkResultClassification
                                .RECONCILIATION_REQUIRED,
                        Optional.of(
                                "owner-outcome-unresolved"
                        )
                )
        );

        assertThrows(
                IllegalStateException.class,
                () -> store.recordAttemptOutcome(
                        "attempt-1",
                        "worker-1",
                        BackgroundWorkResultClassification
                                .RETRY_SAFE,
                        Optional.of(
                                "different-outcome"
                        )
                )
        );

        assertThrows(
                IllegalStateException.class,
                () -> store.rescheduleRetry(
                        "work-1",
                        "worker-1",
                        T0.plusSeconds(120)
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> store.finalise(
                        "work-1",
                        "worker-1",
                        BackgroundWorkResultClassification
                                .RECONCILIATION_REQUIRED,
                        T0.plusSeconds(2)
                )
        );

        assertEquals(
                1,
                count("durable_work_attempt")
        );
    }

    @Test
    void retry_safe_outcome_can_be_rescheduled_and_new_attempt_started_after_next_claim() {
        JooqDurableWorkStore store =
                store();

        store.schedule(
                work("work-1", T0)
        );

        store.claimDue(
                "worker-1",
                T0,
                T0.plusSeconds(60),
                1
        );

        store.startAttempt(
                "attempt-1",
                "work-1",
                "worker-1",
                T0.plusSeconds(1),
                SCHEDULED_PRINCIPAL
        );

        store.recordAttemptOutcome(
                "attempt-1",
                "worker-1",
                BackgroundWorkResultClassification.RETRY_SAFE,
                Optional.of(
                        "owner-proved-no-effect"
                )
        );

        Instant retryAt =
                T0.plusSeconds(120);

        store.rescheduleRetry(
                "work-1",
                "worker-1",
                retryAt
        );

        assertTrue(
                store.claimDue(
                        "worker-2",
                        retryAt.minusNanos(1_000),
                        retryAt.plusSeconds(30),
                        1
                ).isEmpty()
        );

        assertEquals(
                1,
                store.claimDue(
                        "worker-2",
                        retryAt,
                        retryAt.plusSeconds(30),
                        1
                ).size()
        );

        WorkAttempt second =
                store.startAttempt(
                        "attempt-2",
                        "work-1",
                        "worker-2",
                        retryAt.plusSeconds(1),
                        SCHEDULED_PRINCIPAL
                );

        assertEquals(
                "attempt-2",
                second.attemptIdentity()
        );

        assertTrue(
                second.resultClassification()
                        .isEmpty()
        );

        assertEquals(
                2,
                count("durable_work_attempt")
        );
    }

    @Test
    void non_retry_safe_attempt_cannot_be_rescheduled() {
        JooqDurableWorkStore store =
                store();

        store.schedule(
                work("work-1", T0)
        );

        store.claimDue(
                "worker-1",
                T0,
                T0.plusSeconds(60),
                1
        );

        store.startAttempt(
                "attempt-1",
                "work-1",
                "worker-1",
                T0.plusSeconds(1),
                SCHEDULED_PRINCIPAL
        );

        store.recordAttemptOutcome(
                "attempt-1",
                "worker-1",
                BackgroundWorkResultClassification.TERMINAL_FAILURE,
                Optional.empty()
        );

        assertThrows(
                IllegalStateException.class,
                () -> store.rescheduleRetry(
                        "work-1",
                        "worker-1",
                        T0.plusSeconds(120)
                )
        );
    }

    @Test
    void finalisation_requires_current_claim_and_matching_latest_attempt_outcome() {
        JooqDurableWorkStore store =
                store();

        store.schedule(
                work("work-1", T0)
        );

        store.claimDue(
                "worker-1",
                T0,
                T0.plusSeconds(60),
                1
        );

        store.startAttempt(
                "attempt-1",
                "work-1",
                "worker-1",
                T0.plusSeconds(1),
                SCHEDULED_PRINCIPAL
        );

        store.recordAttemptOutcome(
                "attempt-1",
                "worker-1",
                BackgroundWorkResultClassification.SUCCESS,
                Optional.empty()
        );

        assertThrows(
                IllegalStateException.class,
                () -> store.finalise(
                        "work-1",
                        "worker-2",
                        BackgroundWorkResultClassification.SUCCESS,
                        T0.plusSeconds(2)
                )
        );

        assertThrows(
                IllegalStateException.class,
                () -> store.finalise(
                        "work-1",
                        "worker-1",
                        BackgroundWorkResultClassification
                                .TERMINAL_FAILURE,
                        T0.plusSeconds(2)
                )
        );

        store.finalise(
                "work-1",
                "worker-1",
                BackgroundWorkResultClassification.SUCCESS,
                T0.plusSeconds(2)
        );

        assertTrue(
                store.claimDue(
                        "worker-2",
                        T0.plusSeconds(100),
                        T0.plusSeconds(130),
                        1
                ).isEmpty()
        );

        assertEquals(
                "SUCCESS",
                dsl.select(
                                DSL.field(
                                        DSL.name(
                                                "final_classification"
                                        ),
                                        String.class
                                )
                        )
                        .from(
                                DSL.table(
                                        DSL.name(
                                                "durable_work_instruction"
                                        )
                                )
                        )
                        .where(
                                DSL.field(
                                                DSL.name(
                                                        "work_identifier"
                                                ),
                                                String.class
                                        )
                                        .eq("work-1")
                        )
                        .fetchOne(
                                0,
                                String.class
                        )
        );
    }

    @Test
    void no_longer_applicable_is_a_valid_terminal_outcome_not_a_retry_failure() {
        JooqDurableWorkStore store =
                store();

        store.schedule(
                work("work-1", T0)
        );

        store.claimDue(
                "worker-1",
                T0,
                T0.plusSeconds(60),
                1
        );

        store.startAttempt(
                "attempt-1",
                "work-1",
                "worker-1",
                T0.plusSeconds(1),
                SCHEDULED_PRINCIPAL
        );

        store.recordAttemptOutcome(
                "attempt-1",
                "worker-1",
                BackgroundWorkResultClassification
                        .NO_LONGER_APPLICABLE,
                Optional.empty()
        );

        store.finalise(
                "work-1",
                "worker-1",
                BackgroundWorkResultClassification
                        .NO_LONGER_APPLICABLE,
                T0.plusSeconds(2)
        );

        assertEquals(
                "NO_LONGER_APPLICABLE",
                dsl.select(
                                DSL.field(
                                        DSL.name(
                                                "final_classification"
                                        ),
                                        String.class
                                )
                        )
                        .from(
                                DSL.table(
                                        DSL.name(
                                                "durable_work_instruction"
                                        )
                                )
                        )
                        .where(
                                DSL.field(
                                                DSL.name(
                                                        "work_identifier"
                                                ),
                                                String.class
                                        )
                                        .eq("work-1")
                        )
                        .fetchOne(
                                0,
                                String.class
                        )
        );
    }

    @Test
    void contract_affinity_survives_restart_claim_and_exact_replay() {
        var affinity =
                new mainstreet.background.BackgroundWorkContractAffinity(
                        new mainstreet.background
                                .BackgroundWorkContractIdentity(
                                "booking",
                                "reminder-evaluation"
                        ),
                        "release-1"
                );

        var original =
                work("affinity-1", T0)
                        .withContractAffinity(affinity);

        assertEquals(
                original,
                store().schedule(original)
        );

        assertEquals(
                original,
                independentStore()
                        .instruction("affinity-1")
                        .orElseThrow()
        );

        assertEquals(
                original,
                independentStore()
                        .schedule(original)
        );

        assertEquals(
                original,
                independentStore()
                        .claimDue(
                                "worker",
                                T0,
                                T0.plusSeconds(30),
                                1
                        )
                        .getFirst()
                        .instruction()
        );

        assertEquals(
                Optional.of("configuration-1"),
                original.semanticProvenanceReference()
        );
    }

    @Test
    void replay_cannot_replace_contract_release_identity_or_legacy_affinity() {
        var identity =
                new mainstreet.background.BackgroundWorkContractIdentity(
                        "booking",
                        "reminder-evaluation"
                );

        var original =
                work("affinity-2", T0)
                        .withContractAffinity(
                                new mainstreet.background
                                        .BackgroundWorkContractAffinity(
                                        identity,
                                        "release-1"
                                )
                        );

        store().schedule(original);

        assertThrows(
                IllegalStateException.class,
                () -> independentStore()
                        .schedule(
                                original.withContractAffinity(
                                        new mainstreet.background
                                                .BackgroundWorkContractAffinity(
                                                identity,
                                                "release-2"
                                        )
                                )
                        )
        );

        assertThrows(
                IllegalStateException.class,
                () -> independentStore()
                        .schedule(
                                original.withContractAffinity(
                                        new mainstreet.background
                                                .BackgroundWorkContractAffinity(
                                                new mainstreet.background
                                                        .BackgroundWorkContractIdentity(
                                                        "booking",
                                                        "other"
                                                ),
                                                "release-1"
                                        )
                                )
                        )
        );

        assertThrows(
                IllegalStateException.class,
                () -> independentStore()
                        .schedule(
                                work(
                                        "affinity-2",
                                        T0
                                )
                        )
        );

        store().schedule(
                work(
                        "legacy-affinity",
                        T0
                )
        );

        assertTrue(
                independentStore()
                        .instruction(
                                "legacy-affinity"
                        )
                        .orElseThrow()
                        .contractAffinity()
                        .isEmpty()
        );

        assertThrows(
                IllegalStateException.class,
                () -> independentStore()
                        .schedule(
                                work(
                                        "legacy-affinity",
                                        T0
                                ).withContractAffinity(
                                        original.contractAffinity()
                                                .orElseThrow()
                                )
                        )
        );

        assertEquals(
                original,
                independentStore()
                        .instruction("affinity-2")
                        .orElseThrow()
        );

        assertEquals(
                2,
                count("durable_work_instruction")
        );
    }

    @Test
    void database_rejects_partial_blank_or_cross_owner_affinity() {
        store().schedule(
                work(
                        "affinity-invalid",
                        T0
                )
        );

        assertThrows(
                org.jooq.exception.DataAccessException.class,
                () -> dsl.execute(
                        "update durable_work_instruction "
                                + "set contract_owner_identifier = 'booking' "
                                + "where work_identifier = 'affinity-invalid'"
                )
        );

        assertThrows(
                org.jooq.exception.DataAccessException.class,
                () -> dsl.execute(
                        "update durable_work_instruction "
                                + "set contract_owner_identifier = 'booking', "
                                + "contract_identifier = 'review', "
                                + "contract_semantic_release = ' ' "
                                + "where work_identifier = 'affinity-invalid'"
                )
        );

        assertThrows(
                org.jooq.exception.DataAccessException.class,
                () -> dsl.execute(
                        "update durable_work_instruction "
                                + "set contract_owner_identifier = 'other', "
                                + "contract_identifier = 'review', "
                                + "contract_semantic_release = 'r' "
                                + "where work_identifier = 'affinity-invalid'"
                )
        );

        assertTrue(
                independentStore()
                        .instruction(
                                "affinity-invalid"
                        )
                        .orElseThrow()
                        .contractAffinity()
                        .isEmpty()
        );
    }

    private JooqDurableWorkStore store() {
        return new JooqDurableWorkStore(
                dsl,
                transactionManager
        );
    }

    private JooqDurableWorkStore independentStore() {
        return new JooqDurableWorkStore(
                DSL.using(
                        new TransactionAwareDataSourceProxy(
                                authoritativeDataSource
                        ),
                        SQLDialect.POSTGRES
                ),
                new DataSourceTransactionManager(
                        authoritativeDataSource
                )
        );
    }

    private int count(String table) {
        return dsl.fetchCount(
                DSL.table(
                        DSL.name(table)
                )
        );
    }

    private static DurableWorkInstruction work(
            String id,
            Instant dueAt
    ) {
        return new DurableWorkInstruction(
                id,
                "booking",
                BackgroundExecutionScope.MERCHANT,
                Optional.of(MERCHANT_A),
                dueAt,
                "booking.reminder.evaluate",
                "correlation-1",
                Optional.of("booking-1"),
                Optional.of("configuration-1"),
                "booking-reminder-v1",
                OverdueHandling.RE_EVALUATE_CURRENT_STATE,
                T0.minusSeconds(30)
        );
    }

    private static String requiredEnvironment(
            String name
    ) {
        String value =
                System.getenv(name);

        if (value == null
                || value.isBlank()) {
            throw new IllegalStateException(
                    "Missing required PostgreSQL integration-test "
                            + "environment variable: "
                            + name
            );
        }

        return value;
    }
}