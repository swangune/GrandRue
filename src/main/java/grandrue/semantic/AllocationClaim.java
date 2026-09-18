package grandrue.semantic;

import java.time.Instant;
import java.util.Objects;

public record AllocationClaim(
        String identifier,
        AllocationScope scope,
        String useIdentifier,
        Instant claimedAt
) {

    public AllocationClaim {
        requireIdentifier(identifier, "Allocation identifier");
        Objects.requireNonNull(scope);
        requireIdentifier(useIdentifier, "Allocation use identifier");
        Objects.requireNonNull(claimedAt);
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
