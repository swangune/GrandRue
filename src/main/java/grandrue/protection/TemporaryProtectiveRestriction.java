package grandrue.protection;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;

/** Bounded Resource Protection-owned restriction, never Merchant suspension. */
public record TemporaryProtectiveRestriction(
        String restrictionIdentity,
        ProtectionSubject subject,
        Set<ProtectionTarget> targets,
        Instant effectiveFrom,
        Instant expiresAt,
        String reasonClassIdentifier
) {
    public TemporaryProtectiveRestriction {
        if (restrictionIdentity == null || restrictionIdentity.isBlank()) {
            throw new IllegalArgumentException("Restriction identity must not be blank");
        }
        Objects.requireNonNull(subject, "subject");
        targets = Set.copyOf(Objects.requireNonNull(targets, "targets"));
        if (targets.isEmpty()) {
            throw new IllegalArgumentException("Restriction must protect at least one target");
        }
        Objects.requireNonNull(effectiveFrom, "effectiveFrom");
        Objects.requireNonNull(expiresAt, "expiresAt");
        if (!expiresAt.isAfter(effectiveFrom)) {
            throw new IllegalArgumentException("Restriction expiry must be after start");
        }
        if (reasonClassIdentifier == null || reasonClassIdentifier.isBlank()) {
            throw new IllegalArgumentException("Restriction reason class must not be blank");
        }
    }

    public boolean appliesTo(ProtectionTarget target, Instant instant) {
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(instant, "instant");
        return targets.contains(target)
                && !instant.isBefore(effectiveFrom)
                && instant.isBefore(expiresAt);
    }
}
