package grandrue.observability;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/** Bounded diagnostic evidence. It is not an AuditRecord or business fact. */
public record DiagnosticLogObservation(
        Instant occurredAt,
        DiagnosticSeverity severity,
        String componentIdentifier,
        String categoryIdentifier,
        Optional<String> correlationReference,
        Optional<String> safeSubjectReference
) {
    public DiagnosticLogObservation {
        Objects.requireNonNull(occurredAt, "occurredAt");
        Objects.requireNonNull(severity, "severity");
        requireIdentifier(componentIdentifier, "componentIdentifier");
        requireIdentifier(categoryIdentifier, "categoryIdentifier");
        correlationReference = normalise(correlationReference, "correlationReference");
        safeSubjectReference = normalise(safeSubjectReference, "safeSubjectReference");
    }

    private static Optional<String> normalise(Optional<String> value, String label) {
        Objects.requireNonNull(value, label);
        value.ifPresent(item -> requireIdentifier(item, label));
        return value;
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
