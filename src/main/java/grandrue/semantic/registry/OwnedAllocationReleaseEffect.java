package grandrue.semantic.registry;

/** Declares release of one authoritative allocation claim. */
public record OwnedAllocationReleaseEffect(
        String resourceRoleIdentifier
) implements OwnedOperationEffect {
    public OwnedAllocationReleaseEffect {
        if (resourceRoleIdentifier == null || resourceRoleIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Allocation resource-role identifier must not be blank"
            );
        }
    }
}
