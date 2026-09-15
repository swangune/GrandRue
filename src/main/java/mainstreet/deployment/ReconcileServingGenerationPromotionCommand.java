package mainstreet.deployment;

import java.time.Instant;
import java.util.Objects;

public record ReconcileServingGenerationPromotionCommand(
        String transitionIdentifier,
        ObservedServingGeneration observation,
        Instant observedAt
) {
    public ReconcileServingGenerationPromotionCommand {
        if (transitionIdentifier == null || transitionIdentifier.isBlank()) {
            throw new IllegalArgumentException("Transition identifier must not be blank");
        }
        Objects.requireNonNull(observation, "observation");
        Objects.requireNonNull(observedAt, "observedAt");
    }
}
