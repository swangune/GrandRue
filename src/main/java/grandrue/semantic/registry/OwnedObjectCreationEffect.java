package grandrue.semantic.registry;

import java.util.Objects;
import java.util.Optional;

/** Declares creation of one capability-owned Operational Object. */
public record OwnedObjectCreationEffect(
        String targetObjectIdentifier,
        Optional<String> initialStateIdentifier
) implements OwnedOperationEffect {
    public OwnedObjectCreationEffect {
        requireIdentifier(targetObjectIdentifier, "Target object identifier");
        initialStateIdentifier = Objects.requireNonNull(initialStateIdentifier);
        initialStateIdentifier.ifPresent(value ->
                requireIdentifier(value, "Initial state identifier")
        );
    }

    public OwnedObjectCreationEffect(
            String targetObjectIdentifier,
            String initialStateIdentifier
    ) {
        this(targetObjectIdentifier, Optional.ofNullable(initialStateIdentifier));
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
