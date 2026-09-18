package grandrue.semantic.configuration;

/** Capability-scoped stable identity for one registered configuration decision. */
public record CapabilityConfigurationDecisionIdentity(
        String ownerCapabilityIdentifier,
        String decisionIdentifier
) {
    public CapabilityConfigurationDecisionIdentity {
        requireIdentifier(
                ownerCapabilityIdentifier,
                "Configuration decision owner capability identifier"
        );
        requireIdentifier(decisionIdentifier, "Configuration decision identifier");
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
