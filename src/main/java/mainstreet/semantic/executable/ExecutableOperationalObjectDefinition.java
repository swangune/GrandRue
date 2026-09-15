package mainstreet.semantic.executable;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/** Resolved capability-owned Operational Object definition. */
public record ExecutableOperationalObjectDefinition(
        String ownerCapabilityIdentifier,
        String identifier,
        Set<String> stateIdentifiers,
        Optional<String> initialStateIdentifier,
        Optional<ExecutableSchemaReference> schemaReference
) {
    public ExecutableOperationalObjectDefinition {
        requireIdentifier(ownerCapabilityIdentifier, "Owner capability identifier");
        requireIdentifier(identifier, "Operational object identifier");
        stateIdentifiers = Set.copyOf(Objects.requireNonNull(stateIdentifiers));
        initialStateIdentifier = Objects.requireNonNull(initialStateIdentifier);
        schemaReference = Objects.requireNonNull(schemaReference);
        if (stateIdentifiers.isEmpty() != initialStateIdentifier.isEmpty()) {
            throw new IllegalArgumentException(
                    "Operational object lifecycle and initial state must be declared together"
            );
        }
        if (initialStateIdentifier.isPresent()
                && !stateIdentifiers.contains(initialStateIdentifier.get())) {
            throw new IllegalArgumentException(
                    "Initial state must belong to the operational object lifecycle"
            );
        }
        schemaReference.ifPresent(reference -> {
            if (!reference.ownerCapabilityIdentifier().equals(ownerCapabilityIdentifier)) {
                throw new IllegalArgumentException(
                        "Operational object schema must be owned by the same capability"
                );
            }
        });
    }

    public ExecutableOperationalObjectDefinition(
            String ownerCapabilityIdentifier,
            String identifier,
            Set<String> stateIdentifiers,
            String initialStateIdentifier
    ) {
        this(
                ownerCapabilityIdentifier,
                identifier,
                stateIdentifiers,
                Optional.ofNullable(initialStateIdentifier),
                Optional.empty()
        );
    }

    public ExecutableOperationalObjectTypeIdentity identity() {
        return new ExecutableOperationalObjectTypeIdentity(
                ownerCapabilityIdentifier,
                identifier
        );
    }

    public boolean hasLifecycle() {
        return !stateIdentifiers.isEmpty();
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
