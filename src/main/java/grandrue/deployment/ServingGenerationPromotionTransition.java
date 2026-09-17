package grandrue.deployment;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

public record ServingGenerationPromotionTransition(
        String transitionIdentifier,
        String priorGenerationIdentifier,
        String targetGenerationIdentifier,
        ServingGenerationPromotionStatus status,
        long epoch,
        Instant preparedAt,
        Optional<Instant> reconciledAt
) {
    public ServingGenerationPromotionTransition {
        require(transitionIdentifier, "Transition identifier");
        require(priorGenerationIdentifier, "Prior generation identifier");
        require(targetGenerationIdentifier, "Target generation identifier");
        if (priorGenerationIdentifier.equals(targetGenerationIdentifier)) {
            throw new IllegalArgumentException("Promotion target must differ from prior generation");
        }
        Objects.requireNonNull(status, "status");
        if (epoch <= 0) throw new IllegalArgumentException("Epoch must be positive");
        Objects.requireNonNull(preparedAt, "preparedAt");
        reconciledAt = Objects.requireNonNull(reconciledAt, "reconciledAt");
        if (status == ServingGenerationPromotionStatus.PROMOTING && reconciledAt.isPresent()) {
            throw new IllegalArgumentException("PROMOTING transition cannot be reconciled");
        }
        if (status != ServingGenerationPromotionStatus.PROMOTING && reconciledAt.isEmpty()) {
            throw new IllegalArgumentException("Completed transition requires reconciliation time");
        }
    }

    private static void require(String value, String label) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(label + " must not be blank");
    }
}
