package mainstreet.application;

import grandrue.background.ClaimedWork;
import grandrue.background.DurableWorkStore;
import grandrue.background.RegisteredDurableWorkClaimer;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Bounded production worker for the registered Standing Free durable-work contract.
 *
 * <p>The technical claim is contract-scoped. Business authority is re-established
 * by {@link StandingFreeClaimedWorkExecution} for every claimed item. Exceptions
 * are deliberately not translated here: an unresolved started attempt remains
 * available for lease expiry and owner-authoritative recovery.</p>
 */
public final class StandingFreeDurableWorkWorker {
    private final RegisteredDurableWorkClaimer claimer;
    private final DurableWorkStore workStore;
    private final StandingFreeClaimedWorkExecution execution;
    private final Clock clock;
    private final String workerIdentity;
    private final Duration claimLease;
    private final Supplier<String> attemptIdentityFactory;

    public StandingFreeDurableWorkWorker(
            RegisteredDurableWorkClaimer claimer,
            DurableWorkStore workStore,
            StandingFreeClaimedWorkExecution execution,
            Clock clock,
            String workerIdentity,
            Duration claimLease,
            Supplier<String> attemptIdentityFactory) {
        this.claimer = Objects.requireNonNull(claimer, "claimer");
        this.workStore = Objects.requireNonNull(workStore, "workStore");
        this.execution = Objects.requireNonNull(execution, "execution");
        this.clock = Objects.requireNonNull(clock, "clock");
        this.workerIdentity = requireIdentifier(workerIdentity, "workerIdentity");
        this.claimLease = Objects.requireNonNull(claimLease, "claimLease");
        if (claimLease.isZero() || claimLease.isNegative()) {
            throw new IllegalArgumentException("claimLease must be positive");
        }
        this.attemptIdentityFactory = Objects.requireNonNull(
                attemptIdentityFactory, "attemptIdentityFactory");
    }

    public int runOnce(int limit) {
        if (limit < 1) {
            throw new IllegalArgumentException("limit must be positive");
        }
        Instant now = clock.instant();
        List<ClaimedWork> claimed = claimer.claimDue(
                StandingFreeBackgroundWorkContract.IDENTITY,
                workerIdentity,
                now,
                now.plus(claimLease),
                limit);

        int progressed = 0;
        for (ClaimedWork work : claimed) {
            String attemptIdentity = requireIdentifier(
                    attemptIdentityFactory.get(), "attemptIdentity");
            execution.executeClaimed(work, workStore, attemptIdentity);
            progressed++;
        }
        return progressed;
    }

    private static String requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
        return value;
    }
}
