package grandrue.surface;

/** Current actor-authority evidence used only for surface filtering. */
public record ActorAuthoritySurfaceEligibilityEvidence(
        String privilegeIdentifier
) implements ContextualSurfaceEligibilityEvidence {
    public ActorAuthoritySurfaceEligibilityEvidence {
        if (privilegeIdentifier == null || privilegeIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Privilege identifier must not be blank"
            );
        }
    }
}
