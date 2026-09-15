package mainstreet.semantic.capability;

public record CapabilityOperation(
        String identifier,
        String sourceState,
        String targetState
) {

    public CapabilityOperation {

        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Operation identifier required"
            );
        }

        if (sourceState == null || sourceState.isBlank()) {
            throw new IllegalArgumentException(
                    "Source state required"
            );
        }

        if (targetState == null || targetState.isBlank()) {
            throw new IllegalArgumentException(
                    "Target state required"
            );
        }
    }
}