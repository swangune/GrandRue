package grandrue.surface;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/** Immutable server-established evidence for one declared source dependency. */
public record ProjectionSourceEvidence(
        ProjectionSourceDependencyReference sourceReference,
        String evidenceIdentifier,
        Optional<String> observedProgressIdentifier,
        Optional<String> requiredCurrentProgressIdentifier,
        Instant observedAt,
        ProjectionSourceAvailability availability,
        ProjectionSourceCompleteness completeness,
        ProjectionSourceRevocationState revocationState
) {
    public ProjectionSourceEvidence {
        sourceReference = Objects.requireNonNull(
                sourceReference,
                "sourceReference"
        );
        requireIdentifier(evidenceIdentifier, "Evidence identifier");
        observedProgressIdentifier = validatedOptionalIdentifier(
                observedProgressIdentifier,
                "Observed progress identifier"
        );
        requiredCurrentProgressIdentifier = validatedOptionalIdentifier(
                requiredCurrentProgressIdentifier,
                "Required current progress identifier"
        );
        observedAt = Objects.requireNonNull(observedAt, "observedAt");
        availability = Objects.requireNonNull(availability, "availability");
        completeness = Objects.requireNonNull(completeness, "completeness");
        revocationState = Objects.requireNonNull(
                revocationState,
                "revocationState"
        );
        validateNotApplicable(
                availability,
                completeness,
                revocationState,
                observedProgressIdentifier,
                requiredCurrentProgressIdentifier
        );
    }

    public boolean isCurrent() {
        return availability == ProjectionSourceAvailability.AVAILABLE
                && completeness == ProjectionSourceCompleteness.COMPLETE
                && observedProgressIdentifier.isPresent()
                && requiredCurrentProgressIdentifier.isPresent()
                && observedProgressIdentifier.equals(
                        requiredCurrentProgressIdentifier
                )
                && (revocationState
                        == ProjectionSourceRevocationState.CLEAR
                || revocationState
                        == ProjectionSourceRevocationState.NOT_APPLICABLE);
    }

    public boolean isKnownStale() {
        return availability == ProjectionSourceAvailability.AVAILABLE
                && observedProgressIdentifier.isPresent()
                && requiredCurrentProgressIdentifier.isPresent()
                && !observedProgressIdentifier.equals(
                        requiredCurrentProgressIdentifier
                );
    }

    public boolean isNotApplicable() {
        return availability == ProjectionSourceAvailability.NOT_APPLICABLE;
    }

    public boolean hasMissingOrUnverifiableEvidence() {
        if (isNotApplicable()) {
            return false;
        }
        return availability == ProjectionSourceAvailability.UNAVAILABLE
                || completeness != ProjectionSourceCompleteness.COMPLETE
                || observedProgressIdentifier.isEmpty()
                || requiredCurrentProgressIdentifier.isEmpty()
                || revocationState
                        == ProjectionSourceRevocationState.UNVERIFIABLE;
    }

    public boolean hasRevocationConstraint() {
        return revocationState == ProjectionSourceRevocationState.REVOKED
                || revocationState
                        == ProjectionSourceRevocationState.UNVERIFIABLE;
    }

    private static Optional<String> validatedOptionalIdentifier(
            Optional<String> value,
            String label
    ) {
        Optional<String> checked = Objects.requireNonNull(value, label);
        checked.ifPresent(identifier -> requireIdentifier(identifier, label));
        return checked;
    }

    private static void validateNotApplicable(
            ProjectionSourceAvailability availability,
            ProjectionSourceCompleteness completeness,
            ProjectionSourceRevocationState revocationState,
            Optional<String> observedProgressIdentifier,
            Optional<String> requiredCurrentProgressIdentifier
    ) {
        if (availability == ProjectionSourceAvailability.NOT_APPLICABLE) {
            if (completeness
                    != ProjectionSourceCompleteness.NOT_APPLICABLE
                    || revocationState
                    != ProjectionSourceRevocationState.NOT_APPLICABLE
                    || observedProgressIdentifier.isPresent()
                    || requiredCurrentProgressIdentifier.isPresent()) {
                throw new IllegalArgumentException(
                        "Not-applicable source evidence must use only "
                                + "not-applicable classifications and no "
                                + "progress identifiers"
                );
            }
        } else if (completeness
                == ProjectionSourceCompleteness.NOT_APPLICABLE) {
            throw new IllegalArgumentException(
                    "Applicable source evidence cannot have not-applicable "
                            + "completeness"
            );
        }
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
