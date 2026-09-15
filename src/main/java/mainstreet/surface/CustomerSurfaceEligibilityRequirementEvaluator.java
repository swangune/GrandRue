package mainstreet.surface;

import mainstreet.application.MerchantScope;
import mainstreet.runtime.TrustedExecutionContext;

import java.util.Optional;

/**
 * Owner-side evaluator for one owner's registered CUSTOMER Surface eligibility
 * requirements. The owner retains relationship/context semantics; Surface only
 * asks for the current bounded decision.
 */
@FunctionalInterface
public interface CustomerSurfaceEligibilityRequirementEvaluator {

    Optional<Boolean> currentEligibility(
            MerchantScope merchantScope,
            TrustedExecutionContext trustedExecutionContext,
            String requirementIdentifier,
            Optional<String> requestContext
    );
}
