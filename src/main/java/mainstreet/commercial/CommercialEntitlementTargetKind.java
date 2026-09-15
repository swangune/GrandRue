package mainstreet.commercial;

/**
 * Accepted commercial-entitlement target families.
 *
 * <p>These categories describe what kind of already-defined Main Street access
 * point an entitlement references. They do not create or activate that target.</p>
 */
public enum CommercialEntitlementTargetKind {
    CAPABILITY_NEW_ACTIVITY,
    OPERATION_ACCESS,
    FULFILMENT_OR_INTEGRATION_ACCESS,
    PLATFORM_SERVICE_ACCESS,
    PRESENTATION_PRIVILEGE
}
