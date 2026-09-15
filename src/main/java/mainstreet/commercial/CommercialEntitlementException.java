package mainstreet.commercial;

/** Raised when a commercially gated use cannot proceed for the merchant. */
public final class CommercialEntitlementException extends RuntimeException {

    public CommercialEntitlementException(
            String merchantIdentifier,
            CommercialEntitlementIdentity entitlementIdentity
    ) {
        super(
                "Merchant "
                        + merchantIdentifier
                        + " lacks commercial entitlement: "
                        + entitlementIdentity.identifier()
        );
    }
}
