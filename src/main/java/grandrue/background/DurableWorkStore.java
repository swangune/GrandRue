package grandrue.background;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/** Infrastructure-owned durable scheduling/claim/attempt boundary. */
public interface DurableWorkStore {

    DurableWorkInstruction schedule(
            DurableWorkInstruction instruction
    );

    Optional<DurableWorkInstruction> instruction(
            String workIdentity
    );

    List<ClaimedWork> claimDue(
            String workerIdentity,
            Instant now,
            Instant claimExpiresAt,
            int limit
    );

    WorkAttempt startAttempt(
            String attemptIdentity,
            String workIdentity,
            String workerIdentity,
            Instant attemptedAt,
            String principalReference
    );

    WorkAttempt recordAttemptOutcome(
            String attemptIdentity,
            String workerIdentity,
            BackgroundWorkResultClassification classification,
            Optional<String> evidenceReference
    );

    Optional<WorkAttempt> latestAttempt(
            String workIdentity
    );

    void rescheduleRetry(
            String workIdentity,
            String workerIdentity,
            Instant nextAttemptAt
    );

    void finalise(
            String workIdentity,
            String workerIdentity,
            BackgroundWorkResultClassification classification,
            Instant finalisedAt
    );
}