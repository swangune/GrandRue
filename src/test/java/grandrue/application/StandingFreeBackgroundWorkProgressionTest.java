package grandrue.application;

import grandrue.background.BackgroundExecutionScope;
import grandrue.background.BackgroundWorkContractAffinity;
import grandrue.background.BackgroundWorkContractRegistrySnapshot;
import grandrue.background.BackgroundWorkResultClassification;
import grandrue.background.ClaimedWork;
import grandrue.background.DurableWorkInstruction;
import grandrue.background.DurableWorkStore;
import grandrue.background.OverdueHandling;
import grandrue.background.WorkAttempt;
import grandrue.commercial.CommercialEntitlementIdentity;
import grandrue.commercial.StandardPlanLevel;
import grandrue.commercial.StandardPlanRevision;
import grandrue.commercial.StandingFreeBaseline;
import grandrue.commercial.StandingFreeBaselineStore;
import grandrue.merchantaccount.MerchantAccountEstablished;
import grandrue.merchantaccount.MerchantAccountEstablishedEventContract;
import grandrue.merchantaccount.MerchantAccountEstablishedOccurrence;
import grandrue.runtime.ExecutionPrincipal;
import grandrue.runtime.RegisteredScheduledBackgroundWorkExecutionAuthority;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StandingFreeBackgroundWorkProgressionTest {

    private static final Instant T0 =
            Instant.parse("2026-08-24T10:00:00Z");
    private static final Instant DUE_AT =
            Instant.parse("2026-09-12T09:00:00Z");
    private static final Instant EXECUTED_AT =
            DUE_AT.plusSeconds(30);

    private static final String HISTORICAL_RELEASE =
            "standing-free-background-work@0";

    private static final MerchantScope SCOPE =
            new MerchantScope("merchant-1");

    private static final ExecutionPrincipal PRINCIPAL =
            new ExecutionPrincipal(
                    "scheduled/standing-free-reconciliation"
            );

    /**
     * MS-PROT-065 v1.1 §11:
     * sufficient durable WorkAttempt progression evidence must exist before
     * duplicate-sensitive consequential execution begins.
     *
     * MS-PROT-056 v1.6 §5:
     * repeated or lost-acknowledgement Standing Free progression must
     * converge on the same committed baseline rather than multiply effects.
     */
    @Test
    void claimed_execution_starts_attempt_before_owner_consequence_then_records_success_and_finalises() {
        DurableWorkInstruction work = instruction();

        AtomicBoolean ownerCommitted = new AtomicBoolean(false);
        var workStore =
                new RecordingDurableWorkStore(ownerCommitted);
        var baselines =
                new InMemoryBaselineStore(ownerCommitted);

        var ownerOperation =
                new StandingFreeFromMerchantAccountEstablishedHandler(
                        ignored -> {
                            WorkAttempt started =
                                    workStore.latestAttempt(
                                                    work.workIdentity()
                                            )
                                            .orElseThrow(() ->
                                                    new AssertionError(
                                                            "Attempt start must be durable "
                                                                    + "before the owner consequence"
                                                    )
                                            );

                            assertTrue(
                                    started.resultClassification()
                                            .isEmpty()
                            );
                            assertEquals(
                                    PRINCIPAL.identifier(),
                                    started.principalReference()
                            );
                            assertTrue(
                                    workStore.finalClassification == null
                            );

                            return new StandardPlanRevision(
                                    StandardPlanLevel.FREE,
                                    "free-r7",
                                    Set.of(
                                            new CommercialEntitlementIdentity(
                                                    "entitlement-enquiry"
                                            )
                                    )
                            );
                        },
                        baselines,
                        ignored -> "baseline-1"
                );

        var execution =
                new StandingFreeBackgroundWorkExecution(
                        id -> id.equals("establishment-1")
                                ? Optional.of(
                                        authoritativeOccurrence()
                                )
                                : Optional.empty(),
                        historicalContracts(),
                        StandingFreeBackgroundWorkContract::registry,
                        new RegisteredScheduledBackgroundWorkExecutionAuthority(
                                Map.of(
                                        StandingFreeBackgroundWorkContract.IDENTITY,
                                        PRINCIPAL
                                )
                        ),
                        ownerOperation,
                        fixedClock()
                );

        StandingFreeBaseline baseline =
                execution.executeClaimed(
                                new ClaimedWork(
                                        work,
                                        "worker-1",
                                        EXECUTED_AT.plusSeconds(60)
                                ),
                                workStore,
                                "attempt-1"
                        )
                        .orElseThrow();

        assertEquals(
                "baseline-1",
                baseline.baselineIdentity()
        );
        assertEquals(
                SCOPE,
                baseline.merchantScope()
        );

        WorkAttempt completed =
                workStore.latestAttempt(
                                work.workIdentity()
                        )
                        .orElseThrow();

        assertEquals(
                "attempt-1",
                completed.attemptIdentity()
        );
        assertEquals(
                work.workIdentity(),
                completed.workIdentity()
        );
        assertEquals(
                EXECUTED_AT,
                completed.attemptedAt()
        );
        assertEquals(
                PRINCIPAL.identifier(),
                completed.principalReference()
        );
        assertEquals(
                Optional.of(
                        BackgroundWorkResultClassification.SUCCESS
                ),
                completed.resultClassification()
        );

        assertEquals(
                "worker-1",
                workStore.startedByWorker
        );
        assertEquals(
                BackgroundWorkResultClassification.SUCCESS,
                workStore.finalClassification
        );
        assertEquals(
                EXECUTED_AT,
                workStore.finalisedAt
        );
        assertTrue(ownerCommitted.get());
    }

    private static DurableWorkInstruction instruction() {
        return new DurableWorkInstruction(
                "work/standing-free/establishment-1",
                "commercial",
                BackgroundExecutionScope.MERCHANT,
                Optional.of(SCOPE),
                DUE_AT,
                StandingFreeBackgroundWorkContract.DEFINITION
                        .target()
                        .targetIdentifier(),
                "merchant-account-establishment/request-1",
                Optional.of("establishment-1"),
                Optional.of(
                        "merchant-account-established-event/publication-1"
                ),
                StandingFreeBackgroundWorkContract.DEFINITION
                        .retryContractReference(),
                OverdueHandling.RE_EVALUATE_CURRENT_STATE,
                DUE_AT.minusSeconds(30),
                Optional.of(
                        new BackgroundWorkContractAffinity(
                                StandingFreeBackgroundWorkContract.IDENTITY,
                                HISTORICAL_RELEASE
                        )
                )
        );
    }

    private static Function<
            String,
            Optional<BackgroundWorkContractRegistrySnapshot>>
    historicalContracts() {
        var historical =
                new BackgroundWorkContractRegistrySnapshot(
                        HISTORICAL_RELEASE,
                        List.of(
                                StandingFreeBackgroundWorkContract.DEFINITION
                        )
                );

        return release ->
                HISTORICAL_RELEASE.equals(release)
                        ? Optional.of(historical)
                        : Optional.empty();
    }

    private static MerchantAccountEstablishedOccurrence
    authoritativeOccurrence() {
        return new MerchantAccountEstablishedOccurrence(
                "merchant-account-established-event/publication-1",
                MerchantAccountEstablishedEventContract.AFFINITY,
                new MerchantAccountEstablished(
                        "establishment-1",
                        SCOPE,
                        "request-1",
                        T0
                )
        );
    }

    private static Clock fixedClock() {
        return Clock.fixed(
                EXECUTED_AT,
                ZoneOffset.UTC
        );
    }

    private static final class RecordingDurableWorkStore
            implements DurableWorkStore {

        private final AtomicBoolean ownerCommitted;

        private WorkAttempt latestAttempt;
        private String startedByWorker;
        private BackgroundWorkResultClassification
                finalClassification;
        private Instant finalisedAt;

        private RecordingDurableWorkStore(
                AtomicBoolean ownerCommitted
        ) {
            this.ownerCommitted = ownerCommitted;
        }

        @Override
        public DurableWorkInstruction schedule(
                DurableWorkInstruction instruction
        ) {
            throw new AssertionError(
                    "Execution must not reschedule the existing instruction"
            );
        }

        @Override
        public Optional<DurableWorkInstruction> instruction(
                String workIdentity
        ) {
            return Optional.empty();
        }

        @Override
        public List<ClaimedWork> claimDue(
                String workerIdentity,
                Instant now,
                Instant claimExpiresAt,
                int limit
        ) {
            throw new AssertionError(
                    "The test supplies the already-claimed work"
            );
        }

        @Override
        public WorkAttempt startAttempt(
                String attemptIdentity,
                String workIdentity,
                String workerIdentity,
                Instant attemptedAt,
                String principalReference
        ) {
            if (latestAttempt != null) {
                throw new AssertionError(
                        "This test expects exactly one physical attempt"
                );
            }

            if (ownerCommitted.get()) {
                throw new AssertionError(
                        "Attempt start must precede the owner consequence"
                );
            }

            startedByWorker = workerIdentity;

            latestAttempt =
                    new WorkAttempt(
                            attemptIdentity,
                            workIdentity,
                            attemptedAt,
                            principalReference,
                            Optional.empty(),
                            Optional.empty()
                    );

            return latestAttempt;
        }

        @Override
        public WorkAttempt recordAttemptOutcome(
                String attemptIdentity,
                String workerIdentity,
                BackgroundWorkResultClassification classification,
                Optional<String> evidenceReference
        ) {
            if (!ownerCommitted.get()) {
                throw new AssertionError(
                        "SUCCESS outcome must follow the owner consequence"
                );
            }

            if (latestAttempt == null
                    || !latestAttempt
                            .attemptIdentity()
                            .equals(attemptIdentity)) {
                throw new AssertionError(
                        "Outcome must classify the started attempt"
                );
            }

            if (!workerIdentity.equals(startedByWorker)) {
                throw new AssertionError(
                        "Outcome must use the current claim worker"
                );
            }

            if (latestAttempt
                    .resultClassification()
                    .isPresent()) {
                throw new AssertionError(
                        "Attempt outcome must be recorded once"
                );
            }

            latestAttempt =
                    new WorkAttempt(
                            latestAttempt.attemptIdentity(),
                            latestAttempt.workIdentity(),
                            latestAttempt.attemptedAt(),
                            latestAttempt.principalReference(),
                            Optional.of(classification),
                            evidenceReference
                    );

            return latestAttempt;
        }

        @Override
        public Optional<WorkAttempt> latestAttempt(
                String workIdentity
        ) {
            if (latestAttempt == null
                    || !latestAttempt
                            .workIdentity()
                            .equals(workIdentity)) {
                return Optional.empty();
            }

            return Optional.of(latestAttempt);
        }

        @Override
        public void rescheduleRetry(
                String workIdentity,
                String workerIdentity,
                Instant nextAttemptAt
        ) {
            throw new AssertionError(
                    "Successful Standing Free execution must not reschedule"
            );
        }

        @Override
        public void finalise(
                String workIdentity,
                String workerIdentity,
                BackgroundWorkResultClassification classification,
                Instant finalisedAt
        ) {
            if (!ownerCommitted.get()) {
                throw new AssertionError(
                        "Work cannot finalise before the owner consequence"
                );
            }

            if (latestAttempt == null
                    || !latestAttempt
                            .workIdentity()
                            .equals(workIdentity)
                    || !latestAttempt
                            .resultClassification()
                            .equals(
                                    Optional.of(classification)
                            )) {
                throw new AssertionError(
                        "Work may finalise only after its matching "
                                + "attempt outcome is durable"
                );
            }

            if (!workerIdentity.equals(startedByWorker)) {
                throw new AssertionError(
                        "Finalisation must use the current claim worker"
                );
            }

            this.finalClassification = classification;
            this.finalisedAt = finalisedAt;
        }
    }

    private static final class InMemoryBaselineStore
            implements StandingFreeBaselineStore {

        private final Map<MerchantScope, StandingFreeBaseline>
                baselines = new HashMap<>();

        private final AtomicBoolean ownerCommitted;

        private InMemoryBaselineStore(
                AtomicBoolean ownerCommitted
        ) {
            this.ownerCommitted = ownerCommitted;
        }

        @Override
        public StandingFreeBaseline establishIfAbsent(
                StandingFreeBaseline candidate
        ) {
            StandingFreeBaseline committed =
                    baselines.computeIfAbsent(
                            candidate.merchantScope(),
                            ignored -> candidate
                    );

            ownerCommitted.set(true);

            return committed;
        }

        @Override
        public Optional<StandingFreeBaseline> baselineFor(
                MerchantScope merchantScope
        ) {
            return Optional.ofNullable(
                    baselines.get(merchantScope)
            );
        }
    }
}