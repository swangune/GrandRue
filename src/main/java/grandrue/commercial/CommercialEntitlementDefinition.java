package grandrue.commercial;

import mainstreet.commercial.CommercialEntitlementIdentity;
import grandrue.commercial.CommercialEntitlementTargetKind;

import java.util.Objects;

/**
 * Commercial-owned definition of one stable entitlement identity and the
 * already-defined Main Street access point it may satisfy.
 *
 * <p>The definition references a target; it does not create, configure,
 * activate or make the target semantically applicable.</p>
 *
 * <p>Governed by MS-PROT-056 v1.0 and v1.5.</p>
 */
public record CommercialEntitlementDefinition(
        CommercialEntitlementIdentity entitlementIdentity,
        CommercialEntitlementTargetKind targetKind,
        String targetReference,
        String accessPurpose
) {

    public CommercialEntitlementDefinition {
        Objects.requireNonNull(entitlementIdentity, "entitlementIdentity");
        Objects.requireNonNull(targetKind, "targetKind");
        if (targetReference == null || targetReference.isBlank()) {
            throw new IllegalArgumentException(
                    "Commercial entitlement target reference must not be blank"
            );
        }
        if (accessPurpose == null || accessPurpose.isBlank()) {
            throw new IllegalArgumentException(
                    "Commercial entitlement access purpose must not be blank"
            );
        }
    }
}
