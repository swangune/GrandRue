package mainstreet.surface;

import mainstreet.application.MerchantScope;
import mainstreet.runtime.TrustedExecutionContext;

import java.util.Optional;

/**
 * Resolves current satisfaction of one registered owner-qualified CUSTOMER
 * Surface eligibility requirement. Empty means the relationship/context
 * requirement is not safely established and therefore fails closed.
 *
 * <p>This port observes capability/context-owned relationship authority; it
 * does not own or persist customer-business relationships.</p>
 */
@FunctionalInterface
public interface CustomerSurfaceEligibilityAuthority {

    Optional<Boolean> currentEligibility(
            MerchantScope merchantScope,
            TrustedExecutionContext trustedExecutionContext,
            CustomerSurfaceEligibilityRequirementIdentity requirementIdentity,
            Optional<String> requestContext
    );
}
