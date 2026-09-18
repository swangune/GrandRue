package grandrue.surface;

/** Evidence that residual obligations keep a deactivated capability surface relevant. */
public record ResidualObligationSurfaceEligibilityEvidence(
        String capabilityIdentifier
) implements ContextualSurfaceEligibilityEvidence {
    public ResidualObligationSurfaceEligibilityEvidence {
        if (capabilityIdentifier == null || capabilityIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Capability identifier must not be blank"
            );
        }
    }
}
