package mainstreet.surface;

import java.util.Objects;

/** Immutable application-composition binding from requirement owner to evaluator. */
public record CustomerSurfaceEligibilityRequirementBinding(
        String ownerIdentifier,
        CustomerSurfaceEligibilityRequirementEvaluator evaluator
) {
    public CustomerSurfaceEligibilityRequirementBinding {
        if (ownerIdentifier == null || ownerIdentifier.isBlank()) {
            throw new IllegalArgumentException("Owner identifier must not be blank");
        }
        Objects.requireNonNull(evaluator, "evaluator");
    }
}
