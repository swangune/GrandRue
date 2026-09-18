package grandrue.semantic.executable;

/** Resolved contract to establish an authoritative allocation claim. */
public record ExecutableAllocationClaimEffect(
        String resourceRoleIdentifier
) implements ExecutableOperationEffect {
    public ExecutableAllocationClaimEffect {
        if (resourceRoleIdentifier == null || resourceRoleIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Allocation resource-role identifier must not be blank"
            );
        }
    }
}
