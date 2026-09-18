package grandrue.semantic.executable;

import java.util.Objects;
import java.util.Optional;

/**
 * Auditable reason for activating a capability. Required activations retain
 * the immediate source so transitive closure remains explainable.
 */
public record CapabilityActivationReason(
        CapabilityActivationOrigin origin,
        Optional<String> sourceCapabilityIdentifier
) {

    public CapabilityActivationReason {
        Objects.requireNonNull(origin, "origin");
        sourceCapabilityIdentifier = Objects.requireNonNull(
                sourceCapabilityIdentifier,
                "sourceCapabilityIdentifier"
        );
        if (origin == CapabilityActivationOrigin.MERCHANT_SELECTED
                && sourceCapabilityIdentifier.isPresent()) {
            throw new IllegalArgumentException(
                    "A merchant-selected activation has no source capability"
            );
        }
        if (origin == CapabilityActivationOrigin.REQUIRED_BY) {
            requireIdentifier(
                    sourceCapabilityIdentifier.orElseThrow(() ->
                            new IllegalArgumentException(
                                    "A required activation needs its source capability"
                            )
                    )
            );
        }
    }

    public static CapabilityActivationReason merchantSelected() {
        return new CapabilityActivationReason(
                CapabilityActivationOrigin.MERCHANT_SELECTED,
                Optional.empty()
        );
    }

    public static CapabilityActivationReason requiredBy(
            String sourceCapabilityIdentifier
    ) {
        requireIdentifier(sourceCapabilityIdentifier);
        return new CapabilityActivationReason(
                CapabilityActivationOrigin.REQUIRED_BY,
                Optional.of(sourceCapabilityIdentifier)
        );
    }

    private static void requireIdentifier(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Source capability identifier must not be blank"
            );
        }
    }
}
