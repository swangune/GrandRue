package grandrue.application;

import grandrue.background.BackgroundExecutionScope;
import grandrue.background.BackgroundWorkContractIdentity;
import grandrue.background.BackgroundWorkResultClassification;
import grandrue.background.ClaimedWork;
import grandrue.background.DurableWorkInstruction;
import grandrue.background.DurableWorkStore;
import grandrue.background.OverdueHandling;
import grandrue.background.RegisteredDurableWorkClaimer;
import grandrue.background.WorkAttempt;
import grandrue.commercial.StandingFreeBaseline;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StandingFreeDurableWorkWorkerTest {
    private static final Instant NOW = Instant.parse("2026-09-14T08:00:00Z");
    private static final MerchantScope SCOPE = new MerchantScope("merchant-1");

    @Test
    void claims_only_the_registered_standing_free_contract_and_delegates_claimed_work() {
        var store = new RecordingStore();
        var claimer = new RecordingClaimer();
        var calls = new ArrayList<ClaimedWork>();
        var attemptIdentities = new ArrayList<String>();
        StandingFreeClaimedWorkExecution execution = (claimed, workStore, attemptIdentity) -> {
            calls.add(claimed);
            attemptIdentities.add(attemptIdentity);
            assertSame(store, workStore);
            return Optional.empty();
        };
        var attempts = new AtomicInteger();
        var worker = new StandingFreeDurableWorkWorker(
                claimer,
                store,
                execution,
                Clock.fixed(NOW, ZoneOffset.UTC),
                "standing-free-worker-1",
                Duration.ofSeconds(45),
                () -> "attempt-" + attempts.incrementAndGet());

        int progressed = worker.runOnce(5);

        assertEquals(1, progressed);
        assertEquals(StandingFreeBackgroundWorkContract.IDENTITY, claimer.claimedContract);
        assertEquals("standing-free-worker-1", claimer.workerIdentity);
        assertEquals(NOW, claimer.claimedAt);
        assertEquals(NOW.plusSeconds(45), claimer.claimExpiresAt);
        assertEquals(5, claimer.limit);
        assertEquals(1, calls.size());
        assertSame(claimer.claim, calls.get(0));
        assertEquals(List.of("attempt-1"), attemptIdentities);
    }

    @Test
    void owner_failure_is_not_swallowed_or_reclassified_by_the_worker() {
        var store = new RecordingStore();
        var claimer = new RecordingClaimer();
        StandingFreeClaimedWorkExecution execution = (claimed, workStore, attemptIdentity) -> {
            throw new IllegalStateException("simulated owner failure");
        };
        var worker = new StandingFreeDurableWorkWorker(
                claimer,
                store,
                execution,
                Clock.fixed(NOW, ZoneOffset.UTC),
                "standing-free-worker-1",
                Duration.ofSeconds(45),
                () -> "attempt-1");

        assertThrows(IllegalStateException.class, () -> worker.runOnce(1));
        assertEquals(0, store.finaliseCalls);
        assertEquals(0, store.rescheduleCalls);
    }

    private static DurableWorkInstruction instruction() {
        return new DurableWorkInstruction(
                "work/standing-free/establishment-1",
                "commercial",
                BackgroundExecutionScope.MERCHANT,
                Optional.of(SCOPE),
                NOW.minusSeconds(30),
                StandingFreeBackgroundWorkContract.DEFINITION.target().targetIdentifier(),
                "merchant-account-establishment/request-1",
                Optional.of("establishment-1"),
                Optional.of("merchant-account-established-event/publication-1"),
                StandingFreeBackgroundWorkContract.DEFINITION.retryContractReference(),
                OverdueHandling.RE_EVALUATE_CURRENT_STATE,
                NOW.minusSeconds(60),
                Optional.of(StandingFreeBackgroundWorkContract.AFFINITY));
    }

    private static final class RecordingClaimer implements RegisteredDurableWorkClaimer {
        private final ClaimedWork claim = new ClaimedWork(
                instruction(),
                "standing-free-worker-1",
                NOW.plusSeconds(45));
        private BackgroundWorkContractIdentity claimedContract;
        private String workerIdentity;
        private Instant claimedAt;
        private Instant claimExpiresAt;
        private int limit;

        @Override
        public List<ClaimedWork> claimDue(
                BackgroundWorkContractIdentity contractIdentity,
                String workerIdentity,
                Instant now,
                Instant claimExpiresAt,
                int limit) {
            this.claimedContract = contractIdentity;
            this.workerIdentity = workerIdentity;
            this.claimedAt = now;
            this.claimExpiresAt = claimExpiresAt;
            this.limit = limit;
            return List.of(claim);
        }
    }

    private static final class RecordingStore implements DurableWorkStore {
        private int finaliseCalls;
        private int rescheduleCalls;

        @Override
        public DurableWorkInstruction schedule(DurableWorkInstruction instruction) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Optional<DurableWorkInstruction> instruction(String workIdentity) {
            return Optional.empty();
        }

        @Override
        public List<ClaimedWork> claimDue(
                String workerIdentity, Instant now, Instant claimExpiresAt, int limit) {
            throw new AssertionError("unfiltered claim must not be used");
        }

        @Override
        public WorkAttempt startAttempt(
                String attemptIdentity, String workIdentity, String workerIdentity,
                Instant attemptedAt, String principalReference) {
            throw new UnsupportedOperationException();
        }

        @Override
        public WorkAttempt recordAttemptOutcome(
                String attemptIdentity, String workerIdentity,
                BackgroundWorkResultClassification classification,
                Optional<String> evidenceReference) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Optional<WorkAttempt> latestAttempt(String workIdentity) {
            return Optional.empty();
        }

        @Override
        public void rescheduleRetry(
                String workIdentity, String workerIdentity, Instant nextAttemptAt) {
            rescheduleCalls++;
        }

        @Override
        public void finalise(
                String workIdentity, String workerIdentity,
                BackgroundWorkResultClassification classification, Instant finalisedAt) {
            finaliseCalls++;
        }
    }
}
