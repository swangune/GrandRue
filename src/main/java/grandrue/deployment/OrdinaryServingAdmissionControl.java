package grandrue.deployment;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/** Single durable ordinary-cohort generation coordination state. */
public record OrdinaryServingAdmissionControl(
        ServingDeploymentCohort cohort,
        OrdinaryServingAdmissionLifecycle lifecycle,
        long epoch,
        String currentGenerationIdentifier,
        Optional<String> targetGenerationIdentifier,
        Optional<String> promotionTransitionIdentifier,
        Instant lastTransitionAt
) {
    public OrdinaryServingAdmissionControl {
        Objects.requireNonNull(cohort, "cohort");
        Objects.requireNonNull(lifecycle, "lifecycle");
        if (cohort != ServingDeploymentCohort.ORDINARY) {
            throw new IllegalArgumentException("Only ORDINARY is governed");
        }
        if (epoch < 0) {
            throw new IllegalArgumentException("Epoch must not be negative");
        }
        require(currentGenerationIdentifier, "Current generation identifier");
        targetGenerationIdentifier = Objects.requireNonNull(
                targetGenerationIdentifier,
                "targetGenerationIdentifier"
        );
        promotionTransitionIdentifier = Objects.requireNonNull(
                promotionTransitionIdentifier,
                "promotionTransitionIdentifier"
        );
        targetGenerationIdentifier.ifPresent(value -> require(
                value,
                "Target generation identifier"
        ));
        promotionTransitionIdentifier.ifPresent(value -> require(
                value,
                "Promotion transition identifier"
        ));
        if (lifecycle == OrdinaryServingAdmissionLifecycle.STABLE
                && (targetGenerationIdentifier.isPresent()
                || promotionTransitionIdentifier.isPresent())) {
            throw new IllegalArgumentException(
                    "STABLE control cannot retain promotion target state"
            );
        }
        if (lifecycle == OrdinaryServingAdmissionLifecycle.PROMOTING
                && (targetGenerationIdentifier.isEmpty()
                || promotionTransitionIdentifier.isEmpty())) {
            throw new IllegalArgumentException(
                    "PROMOTING control requires target and transition identity"
            );
        }
        Objects.requireNonNull(lastTransitionAt, "lastTransitionAt");
    }

    private static void require(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
