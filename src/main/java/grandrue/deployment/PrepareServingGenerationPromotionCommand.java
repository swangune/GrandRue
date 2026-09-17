package grandrue.deployment;

import java.time.Instant;
import java.util.Objects;

public record PrepareServingGenerationPromotionCommand(
        String transitionIdentifier,
        String expectedCurrentGenerationIdentifier,
        String targetGenerationIdentifier,
        Instant preparedAt
) {
    public PrepareServingGenerationPromotionCommand {
        require(transitionIdentifier, "Transition identifier");
        require(expectedCurrentGenerationIdentifier, "Expected current generation identifier");
        require(targetGenerationIdentifier, "Target generation identifier");
        Objects.requireNonNull(preparedAt, "preparedAt");
    }
    private static void require(String value, String label) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(label + " must not be blank");
    }
}
