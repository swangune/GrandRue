package mainstreet.semantic;

import java.time.Instant;
import java.util.Objects;

public record TimeWindowAllocationScope(
        String subjectIdentifier,
        Instant startsAt,
        Instant endsAt
) implements AllocationScope {

    public TimeWindowAllocationScope {
        if (subjectIdentifier == null
                || subjectIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Allocation subject identifier must not be blank"
            );
        }

        Objects.requireNonNull(startsAt);
        Objects.requireNonNull(endsAt);

        if (!startsAt.isBefore(endsAt)) {
            throw new IllegalArgumentException(
                    "Allocation time window must have positive duration"
            );
        }
    }

    public boolean overlaps(TimeWindowAllocationScope other) {
        Objects.requireNonNull(other);

        return subjectIdentifier.equals(
                other.subjectIdentifier
        ) && startsAt.isBefore(other.endsAt)
                && other.startsAt.isBefore(endsAt);
    }
}
