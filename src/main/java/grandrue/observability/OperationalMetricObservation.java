package grandrue.observability;

import java.time.Instant;
import java.util.Objects;

/** Cardinality-bounded operational measurement; never authoritative business state. */
public record OperationalMetricObservation(
        Instant observedAt,
        String componentIdentifier,
        String metricIdentifier,
        double value,
        String unitIdentifier,
        String scopeIdentifier
) {
    public OperationalMetricObservation {
        Objects.requireNonNull(observedAt, "observedAt");
        requireIdentifier(componentIdentifier, "componentIdentifier");
        requireIdentifier(metricIdentifier, "metricIdentifier");
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Metric value must be finite");
        }
        requireIdentifier(unitIdentifier, "unitIdentifier");
        requireIdentifier(scopeIdentifier, "scopeIdentifier");
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
