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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * MS-PROT-065 v1.1 §§11, 24-27, 32-34 and MS-PROT-056 v1.6 §§5-7.
 */
class StandingFreeDurableWorkExecutionTest {
    private static final Instant T0 = Instant.parse("2026-08-24T10:00:00Z");
    private static final Instant DUE_AT = Instant.parse("2026-09-12T09:00:00Z");
    private static final Instant EXECUTED_AT = DUE_AT.plusSeconds(30);
    private static final String HISTORICAL_RELEASE = "standing-free-background-work@0";
    private static final MerchantScope SCOPE = new MerchantScope("merchant-1");
    private static final ExecutionPrincipal PRINCIPAL =
            new ExecutionPrincipal("scheduled/standing-free-reconciliation");

    @Test
    void attempt_start_is_durable_before_owner_mutation_and_success_is_finalised() {
        var work = new FakeDurableWorkStore(instruction());
        var baselines = new CheckingBaselineStore(work);
        var ownerCalls = new AtomicInteger();
        var execution = execution(baselines, ownerCalls, false);
        ClaimedWork claim = work.claim("worker-1");

        Optional<StandingFreeBaseline> outcome = execution.executeClaimed(
                claim, work, "attempt-1");

        assertTrue(outcome.isPresent());
        assertTrue(baselines.sawStartedAttemptBeforeMutation);
        assertEquals(1, ownerCalls.get());
        assertEquals(Optional.of(BackgroundWorkResultClassification.SUCCESS),
                work.latestAttempt(instruction().workIdentity()).orElseThrow().resultClassification());
        assertEquals(Optional.of(BackgroundWorkResultClassification.SUCCESS),
                work.finalClassification);
    }

    @Test
    void lost_acknowledgement_recovers_committed_baseline_without_duplicate_owner_execution() {
        var work = new FakeDurableWorkStore(instruction());
        var baselines = new CheckingBaselineStore(work);
        var firstOwnerCalls = new AtomicInteger();
        var first = execution(baselines, firstOwnerCalls, false);
        work.failNextOutcomeRecord = true;

        assertThrows(IllegalStateException.class, () -> first.executeClaimed(
                work.claim("worker-1"), work, "attempt-1"));
        assertEquals(1, firstOwnerCalls.get());
        assertTrue(baselines.baselineFor(SCOPE).isPresent());
        assertTrue(work.latestAttempt(instruction().workIdentity()).orElseThrow()
                .resultClassification().isEmpty());

        var recoveringOwnerCalls = new AtomicInteger();
        var recovering = execution(baselines, recoveringOwnerCalls, true);
        Optional<StandingFreeBaseline> recovered = recovering.executeClaimed(
                work.claim("worker-2"), work, "attempt-2-must-not-start");

        assertTrue(recovered.isPresent());
        assertEquals(0, recoveringOwnerCalls.get());
        assertEquals(1, work.attempts.size());
        assertEquals(Optional.of(BackgroundWorkResultClassification.SUCCESS),
                work.attempts.get(0).resultClassification());
        assertEquals(Optional.of(BackgroundWorkResultClassification.SUCCESS),
                work.finalClassification);
    }

    @Test
    void uncertain_failed_attempt_is_not_blindly_repeated_and_absent_owner_evidence_makes_retry_explicit() {
        var work = new FakeDurableWorkStore(instruction());
        var baselines = new CheckingBaselineStore(work);
        var failing = execution(baselines, new AtomicInteger(), true);

        assertThrows(IllegalStateException.class, () -> failing.executeClaimed(
                work.claim("worker-1"), work, "attempt-1"));
        assertTrue(work.latestAttempt(instruction().workIdentity()).orElseThrow()
                .resultClassification().isEmpty());
        assertFalse(work.rescheduled);

        var recovery = execution(baselines, new AtomicInteger(), false);
        Optional<StandingFreeBaseline> outcome = recovery.executeClaimed(
                work.claim("worker-2"), work, "attempt-2-must-not-start");

        assertTrue(outcome.isEmpty());
        assertEquals(1, work.attempts.size());
        assertEquals(Optional.of(BackgroundWorkResultClassification.RETRY_SAFE),
                work.attempts.get(0).resultClassification());
        assertTrue(work.rescheduled);
        assertEquals(0, baselines.baselines.size());
    }

    private static StandingFreeBackgroundWorkExecution execution(
            CheckingBaselineStore baselines,
            AtomicInteger ownerCalls,
            boolean failOwnerBeforeMutation) {
        var handler = new StandingFreeFromMerchantAccountEstablishedHandler(
                ignored -> {
                    ownerCalls.incrementAndGet();
                    if (failOwnerBeforeMutation) {
                        throw new IllegalStateException("catalogue unavailable");
                    }
                    return new StandardPlanRevision(
                            StandardPlanLevel.FREE,
                            "free-r7",
                            Set.of(new CommercialEntitlementIdentity("entitlement-enquiry")));
                },
                baselines,
                ignored -> "baseline-1");
        return new StandingFreeBackgroundWorkExecution(
                id -> id.equals("establishment-1")
                        ? Optional.of(authoritativeOccurrence()) : Optional.empty(),
                historicalContracts(),
                StandingFreeBackgroundWorkContract::registry,
                new RegisteredScheduledBackgroundWorkExecutionAuthority(Map.of(
                        StandingFreeBackgroundWorkContract.IDENTITY, PRINCIPAL)),
                handler,
                Clock.fixed(EXECUTED_AT, ZoneOffset.UTC));
    }

    private static Function<String, Optional<BackgroundWorkContractRegistrySnapshot>>
            historicalContracts() {
        var historical = new BackgroundWorkContractRegistrySnapshot(
                HISTORICAL_RELEASE,
                List.of(StandingFreeBackgroundWorkContract.DEFINITION));
        return release -> HISTORICAL_RELEASE.equals(release)
                ? Optional.of(historical) : Optional.empty();
    }

    private static DurableWorkInstruction instruction() {
        return new DurableWorkInstruction(
                "work/standing-free/establishment-1",
                "commercial",
                BackgroundExecutionScope.MERCHANT,
                Optional.of(SCOPE),
                DUE_AT,
                StandingFreeBackgroundWorkContract.DEFINITION.target().targetIdentifier(),
                "merchant-account-establishment/request-1",
                Optional.of("establishment-1"),
                Optional.of("merchant-account-established-event/publication-1"),
                StandingFreeBackgroundWorkContract.DEFINITION.retryContractReference(),
                OverdueHandling.RE_EVALUATE_CURRENT_STATE,
                DUE_AT.minusSeconds(30),
                Optional.of(new BackgroundWorkContractAffinity(
                        StandingFreeBackgroundWorkContract.IDENTITY,
                        HISTORICAL_RELEASE)));
    }

    private static MerchantAccountEstablishedOccurrence authoritativeOccurrence() {
        return new MerchantAccountEstablishedOccurrence(
                "merchant-account-established-event/publication-1",
                MerchantAccountEstablishedEventContract.AFFINITY,
                new MerchantAccountEstablished("establishment-1", SCOPE, "request-1", T0));
    }

    private static final class CheckingBaselineStore implements StandingFreeBaselineStore {
        private final FakeDurableWorkStore work;
        private final Map<MerchantScope, StandingFreeBaseline> baselines = new HashMap<>();
        private boolean sawStartedAttemptBeforeMutation;

        private CheckingBaselineStore(FakeDurableWorkStore work) {
            this.work = work;
        }

        @Override
        public StandingFreeBaseline establishIfAbsent(StandingFreeBaseline candidate) {
            sawStartedAttemptBeforeMutation = work.latestAttempt(candidate.originatingMerchantAccountEstablishmentIdentity()
                    .equals("establishment-1") ? instruction().workIdentity() : "missing")
                    .filter(attempt -> attempt.resultClassification().isEmpty())
                    .isPresent();
            if (!sawStartedAttemptBeforeMutation) {
                throw new AssertionError("owner mutation began before durable attempt start");
            }
            return baselines.computeIfAbsent(candidate.merchantScope(), ignored -> candidate);
        }

        @Override
        public Optional<StandingFreeBaseline> baselineFor(MerchantScope merchantScope) {
            return Optional.ofNullable(baselines.get(merchantScope));
        }
    }

    private static final class FakeDurableWorkStore implements DurableWorkStore {
        private final DurableWorkInstruction instruction;
        private final List<WorkAttempt> attempts = new ArrayList<>();
        private String currentWorker;
        private Optional<BackgroundWorkResultClassification> finalClassification = Optional.empty();
        private boolean failNextOutcomeRecord;
        private boolean rescheduled;

        private FakeDurableWorkStore(DurableWorkInstruction instruction) {
            this.instruction = instruction;
        }

        private ClaimedWork claim(String workerIdentity) {
            currentWorker = workerIdentity;
            return new ClaimedWork(instruction, workerIdentity, EXECUTED_AT.plusSeconds(60));
        }

        @Override
        public DurableWorkInstruction schedule(DurableWorkInstruction candidate) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Optional<DurableWorkInstruction> instruction(String workIdentity) {
            return instruction.workIdentity().equals(workIdentity)
                    ? Optional.of(instruction) : Optional.empty();
        }

        @Override
        public List<ClaimedWork> claimDue(
                String workerIdentity, Instant now, Instant claimExpiresAt, int limit) {
            throw new UnsupportedOperationException();
        }

        @Override
        public WorkAttempt startAttempt(
                String attemptIdentity,
                String workIdentity,
                String workerIdentity,
                Instant attemptedAt,
                String principalReference) {
            requireWorker(workerIdentity);
            if (!attempts.isEmpty()) {
                WorkAttempt previous = attempts.get(attempts.size() - 1);
                if (previous.resultClassification().isEmpty()) {
                    throw new IllegalStateException("prior attempt unresolved");
                }
                if (!previous.resultClassification().equals(
                        Optional.of(BackgroundWorkResultClassification.RETRY_SAFE))) {
                    throw new IllegalStateException("prior attempt not retry safe");
                }
            }
            WorkAttempt attempt = new WorkAttempt(
                    attemptIdentity,
                    workIdentity,
                    attemptedAt,
                    principalReference,
                    Optional.empty(),
                    Optional.empty());
            attempts.add(attempt);
            return attempt;
        }

        @Override
        public WorkAttempt recordAttemptOutcome(
                String attemptIdentity,
                String workerIdentity,
                BackgroundWorkResultClassification classification,
                Optional<String> evidenceReference) {
            requireWorker(workerIdentity);
            if (failNextOutcomeRecord) {
                failNextOutcomeRecord = false;
                throw new IllegalStateException("simulated acknowledgement loss");
            }
            for (int index = 0; index < attempts.size(); index++) {
                WorkAttempt existing = attempts.get(index);
                if (existing.attemptIdentity().equals(attemptIdentity)) {
                    WorkAttempt classified = new WorkAttempt(
                            existing.attemptIdentity(),
                            existing.workIdentity(),
                            existing.attemptedAt(),
                            existing.principalReference(),
                            Optional.of(classification),
                            evidenceReference);
                    attempts.set(index, classified);
                    return classified;
                }
            }
            throw new IllegalStateException("unknown attempt");
        }

        @Override
        public Optional<WorkAttempt> latestAttempt(String workIdentity) {
            if (attempts.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(attempts.get(attempts.size() - 1));
        }

        @Override
        public void rescheduleRetry(
                String workIdentity, String workerIdentity, Instant nextAttemptAt) {
            requireWorker(workerIdentity);
            rescheduled = true;
            currentWorker = null;
        }

        @Override
        public void finalise(
                String workIdentity,
                String workerIdentity,
                BackgroundWorkResultClassification classification,
                Instant finalisedAt) {
            requireWorker(workerIdentity);
            finalClassification = Optional.of(classification);
            currentWorker = null;
        }

        private void requireWorker(String workerIdentity) {
            if (!workerIdentity.equals(currentWorker)) {
                throw new IllegalStateException("worker lacks current claim");
            }
        }
    }
}
