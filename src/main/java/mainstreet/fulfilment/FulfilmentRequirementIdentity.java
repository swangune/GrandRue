package mainstreet.fulfilment;

/** Capability-scoped identity for one registered Fulfilment Requirement. */
public record FulfilmentRequirementIdentity(
        String ownerCapabilityIdentifier,
        String requirementIdentifier
) {
    public FulfilmentRequirementIdentity {
        requireIdentifier(ownerCapabilityIdentifier, "Owner capability identifier");
        requireIdentifier(requirementIdentifier, "Requirement identifier");
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
