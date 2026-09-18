package grandrue.semantic.registry;

/** Declares a lifecycle transition on a capability-owned Operational Object. */
public record OwnedStateTransitionEffect(
        String targetObjectIdentifier,
        String sourceStateIdentifier,
        String resultingStateIdentifier
) implements OwnedOperationEffect {
    public OwnedStateTransitionEffect {
        requireIdentifier(targetObjectIdentifier, "Target object identifier");
        requireIdentifier(sourceStateIdentifier, "Source state identifier");
        requireIdentifier(resultingStateIdentifier, "Resulting state identifier");
        if (sourceStateIdentifier.equals(resultingStateIdentifier)) {
            throw new IllegalArgumentException(
                    "A state-transition effect must change lifecycle state"
            );
        }
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
