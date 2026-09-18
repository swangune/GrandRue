package grandrue.semantic;

import java.time.Instant;
import java.util.Objects;

public record DomainEvent(
        String identifier,
        String factIdentifier,
        String subjectIdentifier,
        String causationIdentifier,
        Instant occurredAt
) {

    public DomainEvent {
        requireIdentifier(identifier, "Event identifier");
        requireIdentifier(factIdentifier, "Fact identifier");
        requireIdentifier(subjectIdentifier, "Subject identifier");
        requireIdentifier(causationIdentifier, "Causation identifier");
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
