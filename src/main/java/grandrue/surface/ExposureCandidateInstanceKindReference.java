package grandrue.surface;

import java.util.Objects;

/** Owner-qualified stable candidate-instance kind used by Exposure membership contracts. */
public record ExposureCandidateInstanceKindReference(
        String ownerIdentifier,
        String instanceKindIdentifier
) {
    public ExposureCandidateInstanceKindReference {
        require(ownerIdentifier, "ownerIdentifier");
        require(instanceKindIdentifier, "instanceKindIdentifier");
    }

    private static void require(String value, String name) {
        Objects.requireNonNull(value, name);
        if (value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
    }
}
