package grandrue.application;

import grandrue.application.ApplicationOutcomeClassification;
import grandrue.application.ApplicationRequestIdentity;
import grandrue.application.CommittedProgressReference;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Application-layer progression result. It reports known committed references
 * without becoming authoritative for the underlying capability-owned facts.
 */
public record CrossCapabilityApplicationOutcome(
        ApplicationRequestIdentity requestIdentity,
        ApplicationOutcomeClassification classification,
        List<CommittedProgressReference> committedProgress,
        Optional<String> pendingResponsibilityIdentifier,
        Optional<String> reasonCategory
) {
    public CrossCapabilityApplicationOutcome {
        Objects.requireNonNull(requestIdentity, "requestIdentity");
        Objects.requireNonNull(classification, "classification");
        committedProgress = List.copyOf(
                Objects.requireNonNull(committedProgress, "committedProgress")
        );
        pendingResponsibilityIdentifier = normalise(
                pendingResponsibilityIdentifier,
                "pendingResponsibilityIdentifier"
        );
        reasonCategory = normalise(reasonCategory, "reasonCategory");

        if ((classification == ApplicationOutcomeClassification.REJECTED
                || classification == ApplicationOutcomeClassification.CONFLICT)
                && !committedProgress.isEmpty()) {
            throw new IllegalArgumentException(
                    classification + " cannot erase already committed application progress"
            );
        }
        if (classification == ApplicationOutcomeClassification.ACCEPTED_PENDING
                && (committedProgress.isEmpty() || pendingResponsibilityIdentifier.isEmpty())) {
            throw new IllegalArgumentException(
                    "ACCEPTED_PENDING requires committed progress and a pending responsibility"
            );
        }
        if (classification == ApplicationOutcomeClassification.COMPLETED
                && pendingResponsibilityIdentifier.isPresent()) {
            throw new IllegalArgumentException(
                    "COMPLETED cannot retain a pending responsibility"
            );
        }
    }

    private static Optional<String> normalise(Optional<String> value, String label) {
        Objects.requireNonNull(value, label);
        value.ifPresent(item -> {
            if (item.isBlank()) {
                throw new IllegalArgumentException(label + " must not be blank");
            }
        });
        return value;
    }
}
