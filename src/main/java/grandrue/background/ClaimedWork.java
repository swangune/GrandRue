package grandrue.background;

import java.time.Instant;
import java.util.Objects;

/** Technical lease over one due work item; it creates no business authority. */
public record ClaimedWork(
        DurableWorkInstruction instruction,
        String workerIdentity,
        Instant claimExpiresAt
) {
    public ClaimedWork {
        Objects.requireNonNull(instruction, "instruction");
        if (workerIdentity == null || workerIdentity.isBlank()) {
            throw new IllegalArgumentException("workerIdentity must not be blank");
        }
        Objects.requireNonNull(claimExpiresAt, "claimExpiresAt");
    }
}
