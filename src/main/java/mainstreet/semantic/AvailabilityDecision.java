package mainstreet.semantic;

import java.time.Instant;
import java.util.Objects;

public record AvailabilityDecision(
        String requestIdentifier,
        String subjectIdentifier,
        boolean available,
        Instant evaluatedAt
) {

    public AvailabilityDecision {
        requireIdentifier(
                requestIdentifier,
                "Availability request identifier"
        );
        requireIdentifier(
                subjectIdentifier,
                "Availability subject identifier"
        );
        Objects.requireNonNull(evaluatedAt);
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
