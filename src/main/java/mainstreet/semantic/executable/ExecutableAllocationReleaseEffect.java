package mainstreet.semantic.executable;

/** Resolved contract to release an authoritative allocation claim. */
public record ExecutableAllocationReleaseEffect(
        String resourceRoleIdentifier
) implements ExecutableOperationEffect {
    public ExecutableAllocationReleaseEffect {
        if (resourceRoleIdentifier == null || resourceRoleIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Allocation resource-role identifier must not be blank"
            );
        }
    }
}
