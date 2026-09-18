package grandrue.surface;

import java.util.Objects;

/**
 * Release-affined registered definition of one owner-qualified CUSTOMER Surface
 * eligibility requirement under MS-PROT-049 v1.3.
 *
 * <p>The definition establishes semantic registration only. Current
 * relationship/context satisfaction remains runtime authority behind
 * {@link CustomerSurfaceEligibilityAuthority}.</p>
 */
public record CustomerSurfaceEligibilityRequirementDefinition(
        CustomerSurfaceEligibilityRequirementIdentity identity
) {
    public CustomerSurfaceEligibilityRequirementDefinition {
        Objects.requireNonNull(identity, "identity");
    }
}
