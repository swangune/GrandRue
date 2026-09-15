package mainstreet.observability;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Technical execution evidence. Trace identity remains distinct from durable
 * business correlation/process identity.
 */
public record TraceObservation(
        String traceIdentifier,
        String spanIdentifier,
        Optional<String> parentSpanIdentifier,
        String componentIdentifier,
        String operationIdentifier,
        Instant startedAt,
        Instant endedAt,
        String outcomeCategory,
        Optional<String> businessCorrelationReference
) {
    public TraceObservation {
        requireIdentifier(traceIdentifier, "traceIdentifier");
        requireIdentifier(spanIdentifier, "spanIdentifier");
        parentSpanIdentifier = normalise(parentSpanIdentifier, "parentSpanIdentifier");
        requireIdentifier(componentIdentifier, "componentIdentifier");
        requireIdentifier(operationIdentifier, "operationIdentifier");
        Objects.requireNonNull(startedAt, "startedAt");
        Objects.requireNonNull(endedAt, "endedAt");
        if (endedAt.isBefore(startedAt)) {
            throw new IllegalArgumentException("Trace end cannot precede start");
        }
        requireIdentifier(outcomeCategory, "outcomeCategory");
        businessCorrelationReference = normalise(
                businessCorrelationReference,
                "businessCorrelationReference"
        );
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
