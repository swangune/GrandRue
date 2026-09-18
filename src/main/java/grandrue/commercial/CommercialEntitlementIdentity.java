package grandrue.commercial;

/** Stable identity of one Main Street commercial entitlement definition. */
public record CommercialEntitlementIdentity(String identifier) {

    public CommercialEntitlementIdentity {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Commercial entitlement identifier must not be blank"
            );
        }
    }
}
