package grandrue.semantic.executable;

import java.util.Objects;

/** Runtime-ready lifecycle transition on one Operational Object. */
public record ExecutableStateTransitionEffect(
        ExecutableOperationalObjectTypeIdentity targetObjectType,
        String sourceStateIdentifier,
        String resultingStateIdentifier
) implements ExecutableOperationEffect {
    public ExecutableStateTransitionEffect {
        Objects.requireNonNull(targetObjectType);
        requireIdentifier(sourceStateIdentifier, "Source state identifier");
        requireIdentifier(resultingStateIdentifier, "Resulting state identifier");
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
