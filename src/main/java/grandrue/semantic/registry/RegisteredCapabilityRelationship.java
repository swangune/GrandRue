package grandrue.semantic.registry;

import java.util.Objects;

/**
 * Immutable relationship between two capabilities in one semantic registry
 * release. Both endpoints are validated by the owning snapshot.
 */
public record RegisteredCapabilityRelationship(
        String sourceCapabilityIdentifier,
        CapabilityRelationshipType type,
        String targetCapabilityIdentifier
) {

    public RegisteredCapabilityRelationship {
        requireIdentifier(
                sourceCapabilityIdentifier,
                "Source capability identifier"
        );
        Objects.requireNonNull(type, "type");
        requireIdentifier(
                targetCapabilityIdentifier,
                "Target capability identifier"
        );
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
