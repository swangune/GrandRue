package grandrue.protection;

import java.time.Instant;
import java.util.Objects;

/** Authoritative operational accounting for one policy/subject/window. */
public record ProtectionConsumptionState(
        String policyIdentifier,
        int policyVersion,
        ProtectionTarget target,
        ProtectionSubject subject,
        Instant windowStartsAt,
        Instant windowEndsAt,
        long consumedUnits
) {
    public ProtectionConsumptionState {
        if (policyIdentifier == null || policyIdentifier.isBlank()) {
            throw new IllegalArgumentException("Policy identifier must not be blank");
        }
        if (policyVersion < 1) {
            throw new IllegalArgumentException("Policy version must be positive");
        }
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(subject, "subject");
        Objects.requireNonNull(windowStartsAt, "windowStartsAt");
        Objects.requireNonNull(windowEndsAt, "windowEndsAt");
        if (!windowEndsAt.isAfter(windowStartsAt)) {
            throw new IllegalArgumentException("Protection window end must follow start");
        }
        if (consumedUnits < 0) {
            throw new IllegalArgumentException("Consumed units must not be negative");
        }
    }
}
