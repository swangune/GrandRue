package grandrue.semantic;

import java.time.Instant;
import java.util.Objects;

public record AllocationResolution(
        String identifier,
        String claimIdentifier,
        AllocationResolutionType type,
        Instant occurredAt
) {

    public AllocationResolution {
        requireIdentifier(identifier, "Resolution identifier");
        requireIdentifier(
                claimIdentifier,
                "Allocation claim identifier"
        );
        Objects.requireNonNull(type);
        Objects.requireNonNull(occurredAt);
    }

    private static void requireIdentifier(
            String identifier,
            String label
    ) {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException(
                    label + " must not be blank"
            );
        }
    }
}
