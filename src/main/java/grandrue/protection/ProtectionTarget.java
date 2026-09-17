package grandrue.protection;

/**
 * Stable platform-owned identity for one bounded class of work whose shared
 * resource consumption may be independently protected.
 */
public record ProtectionTarget(String identifier) {

    public ProtectionTarget {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Protection target identifier must not be blank"
            );
        }
    }
}
