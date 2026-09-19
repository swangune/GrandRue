package grandrue.infrastructure.persistence.commercial;

import grandrue.application.MerchantScope;
import grandrue.application.StandingFreeBackgroundWorkContract;
import grandrue.application.StandingFreeBackgroundWorkExecution;
import grandrue.application.StandingFreeDurableWorkWorker;
import grandrue.application.StandingFreeFromMerchantAccountEstablishedHandler;
import grandrue.application.TrustedPlatformHumanPrincipal;
import grandrue.background.BackgroundExecutionScope;
import grandrue.background.BackgroundWorkContractAffinity;
import grandrue.background.BackgroundWorkContractRegistrySnapshot;
import grandrue.background.BackgroundWorkResultClassification;
import grandrue.background.DurableWorkInstruction;
import grandrue.background.OverdueHandling;
import grandrue.commercial.CommercialEntitlementIdentity;
import grandrue.commercial.StandardPlanLevel;
import grandrue.commercial.StandardPlanRevision;
import grandrue.commercial.StandingFreeBaseline;
import grandrue.infrastructure.persistence.background.JooqDurableWorkStore;
import grandrue.infrastructure.persistence.background.JooqRegisteredDurableWorkClaimer;
import grandrue.infrastructure.persistence.merchantaccount.JooqMerchantAccountBootstrapStore;
import grandrue.infrastructure.persistence.merchantaccount.JooqMerchantAccountEstablishmentPublicationOutbox;
import grandrue.merchantaccount.MerchantAccountEstablishedEventContract;
import grandrue.merchantaccount.MerchantAccountEstablishedOccurrence;
import grandrue.runtime.ExecutionPrincipal;
import grandrue.runtime.RegisteredScheduledBackgroundWorkExecutionAuthority;
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
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * MS-PROT-065 v1.1,
 * designs/MS-PROT-065 v1.1 — Production Durable Background Work, Timer,
 * Attempt & Retry Execution Contract Amendment.md,
 * §§11, 24-27, 51-55, 59-60; and MS-PROT-056 v1.6,
 * designs/MS-PROT-056 v1.6 — Standing Free Baseline Temporal Anchor Amendment.md,
 * §§3, 5-7.
 */
class StandingFreeBackgroundWorkExecutionIT {
    private static final Instant T0 = Instant.parse("2026-08-24T10:00:00Z");
    private static final Instant DUE_AT = Instant.parse("2026-09-12T09:00:00Z");
    private static final Instant EXECUTED_AT = DUE_AT.plusSeconds(30);
    private static final CommercialEntitlementIdentity ENQUIRY =
            new CommercialEntitlementIdentity("entitlement-enquiry");

    private DSLContext dsl;
    private DataSource dataSource;
    private DataSourceTransactionManager transactionManager;

    @BeforeEach
    void setUp() {
        dataSource = new DriverManagerDataSource(
                requiredEnvironment("GRANDRUE_TEST_POSTGRES_URL"),
                requiredEnvironment("GRANDRUE_TEST_POSTGRES_USER"),
                requiredEnvironment("GRANDRUE_TEST_POSTGRES_PASSWORD"));
        Flyway.configure().dataSource(dataSource).locations("classpath:db/migration")
                .load().migrate();
        dsl = DSL.using(new TransactionAwareDataSourceProxy(dataSource), SQLDialect.POSTGRES);
        transactionManager = new DataSourceTransactionManager(dataSource);
        dsl.execute("truncate table durable_work_attempt, durable_work_instruction, merchant_account cascade");
    }

    @Test
    void reconstructed_due_work_revalidates_current_authority_and_reaches_the_owner_after_restart() {
        MerchantAccountEstablishedOccurrence occurrence = establishOccurrence();
        JooqDurableWorkStore firstStore = workStore();
        firstStore.schedule(instruction(occurrence.fact().merchantScope()));

        DurableWorkInstruction reconstructed = independentWorkStore()
                .claimDue("worker-1", EXECUTED_AT, EXECUTED_AT.plusSeconds(30), 1)
                .getFirst().instruction();
        StandingFreeBaseline first = execution(ordinaryHandler(),
                StandingFreeBackgroundWorkContract::registry).execute(reconstructed);
        StandingFreeBaseline recovered = execution(recoveringHandler(),
                StandingFreeBackgroundWorkContract::registry)
                .execute(independentWorkStore().instruction(reconstructed.workIdentity())
                        .orElseThrow());

        assertEquals(first, recovered);
        assertEquals(T0, first.effectiveFrom());
        assertEquals("establishment-1",
                first.originatingMerchantAccountEstablishmentIdentity());
        assertEquals("free-r7", first.freePlanRevisionIdentity());
        assertEquals(Set.of(ENQUIRY), first.entitlementSnapshot());
        assertEquals(1, count("standing_free_baseline"));
        assertEquals(1, count("durable_work_instruction"));
        assertEquals(0, count("durable_work_attempt"));
    }

    @Test
    void persisted_merchant_scope_is_rejected_when_owner_source_resolves_another_scope() {
        establishOccurrence();
        dsl.execute("insert into merchant_account (merchant_identifier) values ('merchant-forged')");
        DurableWorkInstruction forged = instruction(new MerchantScope("merchant-forged"));
        workStore().schedule(forged);
        DurableWorkInstruction reconstructed = independentWorkStore()
                .instruction(forged.workIdentity()).orElseThrow();

        assertThrows(IllegalStateException.class, () ->
                execution(ordinaryHandler(), StandingFreeBackgroundWorkContract::registry)
                        .execute(reconstructed));

        assertEquals(0, count("standing_free_baseline"));
        assertEquals(0, count("durable_work_attempt"));
    }

    @Test
    void committed_instruction_cannot_execute_after_current_contract_or_principal_withdrawal() {
        MerchantAccountEstablishedOccurrence occurrence = establishOccurrence();
        DurableWorkInstruction committed = workStore().schedule(
                instruction(occurrence.fact().merchantScope()));
        Supplier<BackgroundWorkContractRegistrySnapshot> noCurrentContract = () ->
                new BackgroundWorkContractRegistrySnapshot(
                        StandingFreeBackgroundWorkContract.AFFINITY
                                .semanticRegistryReleaseIdentifier(), List.of());

        assertThrows(IllegalStateException.class, () ->
                execution(ordinaryHandler(), noCurrentContract).execute(committed));
        assertThrows(IllegalStateException.class, () -> new StandingFreeBackgroundWorkExecution(
                new JooqMerchantAccountEstablishmentPublicationOutbox(dsl),
                historicalContracts(), StandingFreeBackgroundWorkContract::registry,
                new RegisteredScheduledBackgroundWorkExecutionAuthority(Map.of()),
                ordinaryHandler(), fixedClock()).execute(committed));

        assertEquals(0, count("standing_free_baseline"));
        assertEquals(0, count("durable_work_attempt"));
    }

    @Test
    void real_worker_commits_attempt_before_owner_execution_and_finalises_success() {
        var occurrence = establishOccurrence();
        var instruction = workStore().schedule(instruction(occurrence.fact().merchantScope()));
        var handler = new StandingFreeFromMerchantAccountEstablishedHandler(
                instant -> {
                    assertEquals(T0, instant);
                    var started = independentWorkStore().latestAttempt(instruction.workIdentity())
                            .orElseThrow();
                    assertEquals("attempt-worker-1", started.attemptIdentity());
                    assertTrue(started.resultClassification().isEmpty());
                    return new StandardPlanRevision(StandardPlanLevel.FREE, "free-r7", Set.of(ENQUIRY));
                }, new JooqStandingFreeBaselineStore(dsl, transactionManager),
                ignored -> "standing-free-1");

        var worker = worker(handler, "worker-1", fixedClock(), "attempt-worker-1");
        assertEquals(1, worker.runOnce(1));
        assertEquals(0, worker.runOnce(1));
        assertFinalisedSuccess(instruction.workIdentity());
        assertEquals(Optional.of(BackgroundWorkResultClassification.SUCCESS),
                independentWorkStore().latestAttempt(instruction.workIdentity())
                        .orElseThrow().resultClassification());
        var baseline = new JooqStandingFreeBaselineStore(dsl, transactionManager)
                .baselineFor(occurrence.fact().merchantScope()).orElseThrow();
        assertEquals(T0, baseline.effectiveFrom());
        assertEquals("free-r7", baseline.freePlanRevisionIdentity());
        assertEquals(Set.of(ENQUIRY), baseline.entitlementSnapshot());
        assertEquals(1, count("durable_work_attempt"));
        assertEquals(1, count("standing_free_baseline"));
    }

    @Test
    void real_worker_recovers_committed_owner_effect_after_expired_claim_without_new_attempt() {
        var occurrence = establishOccurrence();
        var store = workStore();
        var instruction = store.schedule(instruction(occurrence.fact().merchantScope()));
        store.claimDue("lost-worker", EXECUTED_AT, EXECUTED_AT.plusSeconds(30), 1);
        store.startAttempt("lost-attempt", instruction.workIdentity(), "lost-worker",
                EXECUTED_AT, "scheduled/standing-free-reconciliation");
        var committed = ordinaryHandler().handle(occurrence.fact());
        // Crash boundary: the owner commit survives, but no work outcome was acknowledged.
        assertTrue(independentWorkStore().latestAttempt(instruction.workIdentity())
                .orElseThrow().resultClassification().isEmpty());

        var recovery = worker(recoveringHandler(), "replacement-worker",
                Clock.fixed(EXECUTED_AT.plusSeconds(30), ZoneOffset.UTC), "must-not-start");
        assertEquals(1, recovery.runOnce(1));
        assertEquals(0, recovery.runOnce(1));
        assertFinalisedSuccess(instruction.workIdentity());
        var recovered = independentWorkStore().latestAttempt(instruction.workIdentity()).orElseThrow();
        assertEquals("lost-attempt", recovered.attemptIdentity());
        assertEquals(Optional.of(BackgroundWorkResultClassification.SUCCESS), recovered.resultClassification());
        assertEquals(committed, new JooqStandingFreeBaselineStore(dsl, transactionManager)
                .baselineFor(occurrence.fact().merchantScope()).orElseThrow());
        assertEquals(1, count("durable_work_attempt"));
        assertEquals(1, count("standing_free_baseline"));
    }

    @Test
    void real_worker_recovers_pre_effect_failure_then_retries_on_a_separate_claim() {
        var occurrence = establishOccurrence();
        var instruction = workStore().schedule(instruction(occurrence.fact().merchantScope()));
        var unavailableCatalogue = new StandingFreeFromMerchantAccountEstablishedHandler(
                ignored -> { throw new IllegalStateException("catalogue unavailable"); },
                new JooqStandingFreeBaselineStore(dsl, transactionManager), ignored -> "unused");
        var failing = worker(unavailableCatalogue, "failed-worker", fixedClock(), "failed-attempt");
        assertThrows(IllegalStateException.class, () -> failing.runOnce(1));
        assertTrue(independentWorkStore().latestAttempt(instruction.workIdentity())
                .orElseThrow().resultClassification().isEmpty());
        assertEquals(0, count("standing_free_baseline"));

        var recovery = worker(ordinaryHandler(), "replacement-worker",
                Clock.fixed(EXECUTED_AT.plusSeconds(30), ZoneOffset.UTC), "retry-attempt");
        assertEquals(1, recovery.runOnce(1));
        assertEquals(Optional.of(BackgroundWorkResultClassification.RETRY_SAFE),
                independentWorkStore().latestAttempt(instruction.workIdentity())
                        .orElseThrow().resultClassification());
        assertEquals(1, count("durable_work_attempt"));
        assertEquals(0, count("standing_free_baseline"));

        assertEquals(1, recovery.runOnce(1));
        assertEquals(0, recovery.runOnce(1));
        assertEquals(Optional.of(BackgroundWorkResultClassification.SUCCESS),
                independentWorkStore().latestAttempt(instruction.workIdentity())
                        .orElseThrow().resultClassification());
        assertEquals(2, count("durable_work_attempt"));
        assertEquals(1, count("standing_free_baseline"));
        assertFinalisedSuccess(instruction.workIdentity());
    }

    @Test
    void expired_worker_resuming_after_replacement_completion_cannot_duplicate_the_owner_effect()
            throws Exception {
        var occurrence = establishOccurrence();
        var instruction = workStore().schedule(instruction(occurrence.fact().merchantScope()));
        var ownerEntered = new CountDownLatch(1);
        var resumeOwner = new CountDownLatch(1);
        var delayedOwner = new StandingFreeFromMerchantAccountEstablishedHandler(
                instant -> {
                    ownerEntered.countDown();
                    try {
                        assertTrue(resumeOwner.await(10, TimeUnit.SECONDS), "owner was not released");
                    } catch (InterruptedException interrupted) {
                        Thread.currentThread().interrupt();
                        throw new IllegalStateException("owner interrupted", interrupted);
                    }
                    assertEquals(T0, instant);
                    return new StandardPlanRevision(StandardPlanLevel.FREE, "free-r7", Set.of(ENQUIRY));
                }, new JooqStandingFreeBaselineStore(dsl, transactionManager),
                ignored -> "late-baseline-must-not-be-inserted");
        var original = worker(delayedOwner, "original-worker", fixedClock(), "original-attempt");
        var executor = Executors.newSingleThreadExecutor();
        try {
            var originalRun = executor.submit(() -> original.runOnce(1));
            assertTrue(ownerEntered.await(10, TimeUnit.SECONDS), "owner was not reached");
            assertTrue(independentWorkStore().latestAttempt(instruction.workIdentity())
                    .orElseThrow().resultClassification().isEmpty());

            var replacement = worker(ordinaryHandler(), "replacement-worker",
                    Clock.fixed(EXECUTED_AT.plusSeconds(30), ZoneOffset.UTC), "replacement-attempt");
            assertEquals(1, replacement.runOnce(1));
            assertEquals(Optional.of(BackgroundWorkResultClassification.RETRY_SAFE),
                    independentWorkStore().latestAttempt(instruction.workIdentity())
                            .orElseThrow().resultClassification());
            assertEquals(0, count("standing_free_baseline"));
            assertEquals(1, replacement.runOnce(1));
            assertFinalisedSuccess(instruction.workIdentity());

            resumeOwner.countDown();
            var staleAcknowledgement = assertThrows(ExecutionException.class,
                    () -> originalRun.get(10, TimeUnit.SECONDS));
            assertTrue(staleAcknowledgement.getCause() instanceof IllegalStateException);
            var baseline = new JooqStandingFreeBaselineStore(dsl, transactionManager)
                    .baselineFor(occurrence.fact().merchantScope()).orElseThrow();
            assertEquals("standing-free-1", baseline.baselineIdentity());
            assertEquals(T0, baseline.effectiveFrom());
            assertEquals("free-r7", baseline.freePlanRevisionIdentity());
            assertEquals(Set.of(ENQUIRY), baseline.entitlementSnapshot());
            assertEquals(1, count("standing_free_baseline"));
            assertEquals(2, count("durable_work_attempt"));
            var latest = independentWorkStore().latestAttempt(instruction.workIdentity()).orElseThrow();
            assertEquals("replacement-attempt", latest.attemptIdentity());
            assertEquals(Optional.of(BackgroundWorkResultClassification.SUCCESS), latest.resultClassification());
            assertFinalisedSuccess(instruction.workIdentity());
        } finally {
            resumeOwner.countDown();
            executor.shutdownNow();
            assertTrue(executor.awaitTermination(10, TimeUnit.SECONDS), "worker did not terminate");
        }
    }

    private void assertFinalisedSuccess(String workIdentity) {
        assertEquals("SUCCESS", dsl.fetchOne(
                "select final_classification from durable_work_instruction "
                        + "where work_identifier = ? and finalised_at is not null", workIdentity)
                .get(0, String.class));
        assertTrue(independentWorkStore().claimDue("later-worker", EXECUTED_AT.plusSeconds(3600),
                EXECUTED_AT.plusSeconds(3660), 1).isEmpty());
    }

    private StandingFreeDurableWorkWorker worker(
            StandingFreeFromMerchantAccountEstablishedHandler handler,
            String identity, Clock clock, String attemptIdentity) {
        var store = independentWorkStore();
        var execution = new StandingFreeBackgroundWorkExecution(
                new JooqMerchantAccountEstablishmentPublicationOutbox(dsl),
                historicalContracts(), StandingFreeBackgroundWorkContract::registry,
                new RegisteredScheduledBackgroundWorkExecutionAuthority(Map.of(
                        StandingFreeBackgroundWorkContract.IDENTITY,
                        new ExecutionPrincipal("scheduled/standing-free-reconciliation"))),
                handler, clock);
        return new StandingFreeDurableWorkWorker(
                new JooqRegisteredDurableWorkClaimer(dsl, transactionManager, store),
                store, execution, clock, identity, Duration.ofSeconds(30), () -> attemptIdentity);
    }

    private StandingFreeBackgroundWorkExecution execution(
            StandingFreeFromMerchantAccountEstablishedHandler handler,
            Supplier<BackgroundWorkContractRegistrySnapshot> currentContracts) {
        return new StandingFreeBackgroundWorkExecution(
                new JooqMerchantAccountEstablishmentPublicationOutbox(dsl),
                historicalContracts(), currentContracts,
                new RegisteredScheduledBackgroundWorkExecutionAuthority(Map.of(
                        StandingFreeBackgroundWorkContract.IDENTITY,
                        new ExecutionPrincipal("scheduled/standing-free-reconciliation"))),
                handler, fixedClock());
    }

    private Function<String, Optional<BackgroundWorkContractRegistrySnapshot>>
            historicalContracts() {
        return release -> StandingFreeBackgroundWorkContract.AFFINITY
                .semanticRegistryReleaseIdentifier().equals(release)
                ? Optional.of(StandingFreeBackgroundWorkContract.registry())
                : Optional.empty();
    }

    private StandingFreeFromMerchantAccountEstablishedHandler ordinaryHandler() {
        return new StandingFreeFromMerchantAccountEstablishedHandler(
                ignored -> new StandardPlanRevision(
                        StandardPlanLevel.FREE, "free-r7", Set.of(ENQUIRY)),
                new JooqStandingFreeBaselineStore(dsl, transactionManager),
                ignored -> "standing-free-1");
    }

    private StandingFreeFromMerchantAccountEstablishedHandler recoveringHandler() {
        return new StandingFreeFromMerchantAccountEstablishedHandler(
                ignored -> { throw new AssertionError("Recovery must not query the catalogue"); },
                new JooqStandingFreeBaselineStore(dsl, transactionManager),
                ignored -> { throw new AssertionError("Recovery must reuse committed identity"); });
    }

    private MerchantAccountEstablishedOccurrence establishOccurrence() {
        var bootstrap = new JooqMerchantAccountBootstrapStore(
                dsl, transactionManager, Clock.fixed(T0, ZoneOffset.UTC),
                () -> "establishment-1", () -> "publication-1");
        bootstrap.establishIfAbsent(
                "request-1", new TrustedPlatformHumanPrincipal("identity-1"));
        return new JooqMerchantAccountEstablishmentPublicationOutbox(dsl)
                .intentForEstablishment("establishment-1")
                .orElseThrow()
                .registeredOccurrence(MerchantAccountEstablishedEventContract.registry())
                .orElseThrow();
    }

    private DurableWorkInstruction instruction(MerchantScope scope) {
        return new DurableWorkInstruction(
                "work/standing-free/establishment-1",
                "commercial",
                BackgroundExecutionScope.MERCHANT,
                Optional.of(scope),
                DUE_AT,
                StandingFreeBackgroundWorkContract.DEFINITION.target().targetIdentifier(),
                "request-1",
                Optional.of("establishment-1"),
                Optional.of("merchant-account-established-event/publication-1"),
                StandingFreeBackgroundWorkContract.DEFINITION.retryContractReference(),
                OverdueHandling.RE_EVALUATE_CURRENT_STATE,
                DUE_AT.minusSeconds(30),
                Optional.of(new BackgroundWorkContractAffinity(
                        StandingFreeBackgroundWorkContract.IDENTITY,
                        StandingFreeBackgroundWorkContract.AFFINITY
                                .semanticRegistryReleaseIdentifier())));
    }

    private JooqDurableWorkStore workStore() {
        return new JooqDurableWorkStore(dsl, transactionManager);
    }

    private JooqDurableWorkStore independentWorkStore() {
        return new JooqDurableWorkStore(
                DSL.using(new TransactionAwareDataSourceProxy(dataSource), SQLDialect.POSTGRES),
                new DataSourceTransactionManager(dataSource));
    }

    private int count(String table) {
        return dsl.fetchCount(DSL.table(DSL.name(table)));
    }

    private static Clock fixedClock() {
        return Clock.fixed(EXECUTED_AT, ZoneOffset.UTC);
    }

    private static String requiredEnvironment(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Missing required PostgreSQL test environment: " + name);
        }
        return value;
    }
}
