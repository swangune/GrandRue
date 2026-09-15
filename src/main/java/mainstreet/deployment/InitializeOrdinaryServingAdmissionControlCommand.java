package mainstreet.deployment;

import java.time.Instant;
import java.util.Objects;

public record InitializeOrdinaryServingAdmissionControlCommand(
        String initialGenerationIdentifier,
        Instant initializedAt
) {
    public InitializeOrdinaryServingAdmissionControlCommand {
        require(initialGenerationIdentifier, "Initial generation identifier");
        Objects.requireNonNull(initializedAt, "initializedAt");
    }
    private static void require(String value, String label) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(label + " must not be blank");
    }
}
