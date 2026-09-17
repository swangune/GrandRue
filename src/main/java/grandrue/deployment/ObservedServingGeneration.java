package grandrue.deployment;

import java.util.Objects;
import java.util.Optional;

public record ObservedServingGeneration(
        ServingGenerationObservationKind kind,
        Optional<String> generationIdentifier
) {
    public ObservedServingGeneration {
        Objects.requireNonNull(kind, "kind");
        generationIdentifier = Objects.requireNonNull(generationIdentifier, "generationIdentifier");
        if (kind == ServingGenerationObservationKind.HOMOGENEOUS && generationIdentifier.isEmpty()) {
            throw new IllegalArgumentException("Homogeneous observation requires a generation");
        }
        if (kind != ServingGenerationObservationKind.HOMOGENEOUS && generationIdentifier.isPresent()) {
            throw new IllegalArgumentException("Non-homogeneous observation cannot identify a generation");
        }
        generationIdentifier.ifPresent(value -> require(value, "Generation identifier"));
    }

    public static ObservedServingGeneration homogeneous(String generationIdentifier) {
        return new ObservedServingGeneration(ServingGenerationObservationKind.HOMOGENEOUS, Optional.of(generationIdentifier));
    }

    public static ObservedServingGeneration mixed() {
        return new ObservedServingGeneration(ServingGenerationObservationKind.MIXED, Optional.empty());
    }

    public static ObservedServingGeneration unknown() {
        return new ObservedServingGeneration(ServingGenerationObservationKind.UNKNOWN, Optional.empty());
    }

    private static void require(String value, String label) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(label + " must not be blank");
    }
}
