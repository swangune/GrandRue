package mainstreet.semantic.registry;

/** Declares establishment of one authoritative allocation claim. */
public record OwnedAllocationClaimEffect(
        String resourceRoleIdentifier
) implements OwnedOperationEffect {
    public OwnedAllocationClaimEffect {
        if (resourceRoleIdentifier == null || resourceRoleIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Allocation resource-role identifier must not be blank"
            );
        }
    }
}
