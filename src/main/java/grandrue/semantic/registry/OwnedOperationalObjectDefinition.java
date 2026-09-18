package grandrue.semantic.registry;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Capability-owned identity-bearing business object. Lifecycle and structured
 * data schema are independent optional concerns.
 */
public record OwnedOperationalObjectDefinition(
        String identifier,
        Set<String> stateIdentifiers,
        Optional<String> initialStateIdentifier,
        Optional<OwnedSchemaReference> schemaReference
) {
    public OwnedOperationalObjectDefinition {
        requireIdentifier(identifier, "Operational object identifier");
        stateIdentifiers = Set.copyOf(Objects.requireNonNull(stateIdentifiers));
        initialStateIdentifier = Objects.requireNonNull(initialStateIdentifier);
        schemaReference = Objects.requireNonNull(schemaReference);

        for (String stateIdentifier : stateIdentifiers) {
            requireIdentifier(stateIdentifier, "State identifier");
        }
        validateLifecycle(stateIdentifiers, initialStateIdentifier);
    }

    public OwnedOperationalObjectDefinition(
            String identifier,
            Set<String> stateIdentifiers,
            String initialStateIdentifier
    ) {
        this(
                identifier,
                stateIdentifiers,
                Optional.ofNullable(initialStateIdentifier),
                Optional.empty()
        );
    }

    public OwnedOperationalObjectDefinition(
            String identifier,
            Set<String> stateIdentifiers,
            String initialStateIdentifier,
            OwnedSchemaReference schemaReference
    ) {
        this(
                identifier,
                stateIdentifiers,
                Optional.ofNullable(initialStateIdentifier),
                Optional.ofNullable(schemaReference)
        );
    }

    public boolean hasLifecycle() {
        return !stateIdentifiers.isEmpty();
    }

    private static void validateLifecycle(
            Set<String> states,
            Optional<String> initialState
    ) {
        if (states.isEmpty()) {
            if (initialState.isPresent()) {
                throw new IllegalArgumentException(
                        "A stateless operational object cannot declare an initial state"
                );
            }
            return;
        }
        String initial = initialState.orElseThrow(() ->
                new IllegalArgumentException(
                        "A lifecycle-bearing operational object requires an initial state"
                )
        );
        requireIdentifier(initial, "Initial state identifier");
        if (!states.contains(initial)) {
            throw new IllegalArgumentException(
                    "Initial state must belong to the operational object lifecycle"
            );
        }
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
