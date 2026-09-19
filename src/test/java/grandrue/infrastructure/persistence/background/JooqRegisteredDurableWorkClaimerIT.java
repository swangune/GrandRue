package grandrue.infrastructure.persistence.background;

import grandrue.application.MerchantScope;
import grandrue.application.StandingFreeBackgroundWorkContract;
import grandrue.background.BackgroundExecutionScope;
import grandrue.background.BackgroundWorkContractAffinity;
import grandrue.background.BackgroundWorkContractIdentity;
import grandrue.background.DurableWorkInstruction;
import grandrue.background.OverdueHandling;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * MS-PROT-065 v1.1,
 * designs/MS-PROT-065 v1.1 — Production Durable Background Work, Timer,
 * Attempt & Retry Execution Contract Amendment.md,
 * §51 — Worker Claiming; §53 — Multiple Workers; §59 — Restart Recovery.
 */
class JooqRegisteredDurableWorkClaimerIT {
    private static final Instant T0 = Instant.parse("2026-09-14T08:30:00Z");
    private static final MerchantScope SCOPE = new MerchantScope("merchant-a");

    private DSLContext dsl;
    private DataSourceTransactionManager transactionManager;

    @BeforeEach
    void setUp() {
        DataSource authoritativeDataSource = new DriverManagerDataSource(
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_URL"),
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_USER"),
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_PASSWORD"));
        Flyway.configure()
                .dataSource(authoritativeDataSource)
                .locations("classpath:db/migration")
                .load()
                .migrate();
        dsl = DSL.using(
                new TransactionAwareDataSourceProxy(authoritativeDataSource),
                SQLDialect.POSTGRES);
        transactionManager = new DataSourceTransactionManager(authoritativeDataSource);
        dsl.execute("truncate table durable_work_attempt, durable_work_instruction");
        dsl.execute("insert into merchant_account (merchant_identifier) values ('merchant-a') on conflict do nothing");
    }

    @Test
    void contract_scoped_claim_never_leases_unrelated_or_unregistered_work() {
        var store = new JooqDurableWorkStore(dsl, transactionManager);
        store.schedule(standingFree("standing-1", StandingFreeBackgroundWorkContract.AFFINITY));
        store.schedule(standingFree(
                "standing-historical",
                new BackgroundWorkContractAffinity(
                        StandingFreeBackgroundWorkContract.IDENTITY,
                        "standing-free-background-work@0")));
        store.schedule(otherRegistered("booking-1"));
        store.schedule(unregistered("legacy-1"));

        var claimer = new JooqRegisteredDurableWorkClaimer(dsl, transactionManager, store);
        var standingClaims = claimer.claimDue(
                StandingFreeBackgroundWorkContract.IDENTITY,
                "standing-worker",
                T0,
                T0.plusSeconds(60),
                10);

        assertEquals(2, standingClaims.size());
        assertEquals(
                java.util.Set.of("standing-1", "standing-historical"),
                standingClaims.stream()
                        .map(claim -> claim.instruction().workIdentity())
                        .collect(java.util.stream.Collectors.toSet()));

        var remaining = store.claimDue(
                "general-worker",
                T0,
                T0.plusSeconds(60),
                10);
        assertEquals(
                java.util.Set.of("booking-1", "legacy-1"),
                remaining.stream()
                        .map(claim -> claim.instruction().workIdentity())
                        .collect(java.util.stream.Collectors.toSet()));
    }

    @Test
    void active_contract_claim_is_not_stolen_but_expired_claim_is_recoverable_by_another_worker() {
        var store = new JooqDurableWorkStore(dsl, transactionManager);
        store.schedule(standingFree("standing-reclaim", StandingFreeBackgroundWorkContract.AFFINITY));
        var claimer = new JooqRegisteredDurableWorkClaimer(dsl, transactionManager, store);

        var first = claimer.claimDue(
                StandingFreeBackgroundWorkContract.IDENTITY,
                "worker-1",
                T0,
                T0.plusSeconds(30),
                1);
        assertEquals(1, first.size());
        assertEquals("standing-reclaim", first.get(0).instruction().workIdentity());

        assertTrue(claimer.claimDue(
                StandingFreeBackgroundWorkContract.IDENTITY,
                "worker-2",
                T0.plusSeconds(29),
                T0.plusSeconds(60),
                1).isEmpty());

        var reclaimed = claimer.claimDue(
                StandingFreeBackgroundWorkContract.IDENTITY,
                "worker-2",
                T0.plusSeconds(30),
                T0.plusSeconds(90),
                1);
        assertEquals(1, reclaimed.size());
        assertEquals("standing-reclaim", reclaimed.get(0).instruction().workIdentity());
        assertEquals("worker-2", reclaimed.get(0).workerIdentity());
    }

    @Test
    void expired_claim_cannot_start_a_consequential_attempt_before_reclaim() {
        var store = new JooqDurableWorkStore(dsl, transactionManager);
        store.schedule(standingFree("standing-expired", StandingFreeBackgroundWorkContract.AFFINITY));
        var claimer = new JooqRegisteredDurableWorkClaimer(dsl, transactionManager, store);
        claimer.claimDue(
                StandingFreeBackgroundWorkContract.IDENTITY,
                "worker-1",
                T0,
                T0.plusSeconds(30),
                1);

        assertThrows(IllegalStateException.class, () -> store.startAttempt(
                "attempt-after-expiry",
                "standing-expired",
                "worker-1",
                T0.plusSeconds(30),
                "scheduled/standing-free-reconciliation"));

        assertTrue(store.latestAttempt("standing-expired").isEmpty());
    }

    private static DurableWorkInstruction standingFree(
            String workIdentity,
            BackgroundWorkContractAffinity affinity) {
        return new DurableWorkInstruction(
                workIdentity,
                "commercial",
                BackgroundExecutionScope.MERCHANT,
                Optional.of(SCOPE),
                T0,
                StandingFreeBackgroundWorkContract.DEFINITION.target().targetIdentifier(),
                "correlation-" + workIdentity,
                Optional.of("establishment-1"),
                Optional.of("publication-1"),
                StandingFreeBackgroundWorkContract.DEFINITION.retryContractReference(),
                OverdueHandling.RE_EVALUATE_CURRENT_STATE,
                T0.minusSeconds(30),
                Optional.of(affinity));
    }

    private static DurableWorkInstruction otherRegistered(String workIdentity) {
        var affinity = new BackgroundWorkContractAffinity(
                new BackgroundWorkContractIdentity("booking", "reminder-evaluation"),
                "booking-background-work@1");
        return new DurableWorkInstruction(
                workIdentity,
                "booking",
                BackgroundExecutionScope.MERCHANT,
                Optional.of(SCOPE),
                T0,
                "booking.reminder-evaluation",
                "correlation-" + workIdentity,
                Optional.empty(),
                Optional.empty(),
                "booking-retry-policy",
                OverdueHandling.RE_EVALUATE_CURRENT_STATE,
                T0.minusSeconds(30),
                Optional.of(affinity));
    }

    private static DurableWorkInstruction unregistered(String workIdentity) {
        return new DurableWorkInstruction(
                workIdentity,
                "booking",
                BackgroundExecutionScope.MERCHANT,
                Optional.of(SCOPE),
                T0,
                "legacy-booking-work",
                "correlation-" + workIdentity,
                Optional.empty(),
                Optional.empty(),
                "legacy-retry-policy",
                OverdueHandling.RE_EVALUATE_CURRENT_STATE,
                T0.minusSeconds(30));
    }

    private static String requiredEnvironment(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required environment variable: " + name);
        }
        return value;
    }
}
