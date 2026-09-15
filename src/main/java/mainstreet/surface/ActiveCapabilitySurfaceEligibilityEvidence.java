package mainstreet.surface;

/** Static evidence that the contribution owner is active in the resolved model. */
public record ActiveCapabilitySurfaceEligibilityEvidence(
        String capabilityIdentifier
) implements StaticSurfaceEligibilityEvidence {
    public ActiveCapabilitySurfaceEligibilityEvidence {
        if (capabilityIdentifier == null || capabilityIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Capability identifier must not be blank"
            );
        }
    }
}
