package grandrue.observability;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/** Operational health evidence about one explicitly named subject/scope. */
public record HealthObservation(
        String subjectIdentifier,
        OperationalHealthDimension dimension,
        OperationalHealthState state,
        Instant observedAt,
        Optional<String> evidenceCategory
) {
    public HealthObservation {
        requireIdentifier(subjectIdentifier, "subjectIdentifier");
        Objects.requireNonNull(dimension, "dimension");
        Objects.requireNonNull(state, "state");
        Objects.requireNonNull(observedAt, "observedAt");
        evidenceCategory = Objects.requireNonNull(evidenceCategory, "evidenceCategory");
        evidenceCategory.ifPresent(value -> requireIdentifier(value, "evidenceCategory"));
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
