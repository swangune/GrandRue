package grandrue.surface;

import grandrue.application.MerchantScope;
import grandrue.runtime.TrustedExecutionContext;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Immutable owner-routed implementation of the Surface-facing CUSTOMER
 * eligibility authority under MS-PROT-049 v1.3.
 *
 * <p>The composite owns no customer relationship semantics. It routes an
 * owner-qualified requirement to the registered owner evaluator. A missing
 * owner evaluator affects only that requirement and fails closed.</p>
 */
public final class CompositeCustomerSurfaceEligibilityAuthority
        implements CustomerSurfaceEligibilityAuthority {

    private final Map<String, CustomerSurfaceEligibilityRequirementEvaluator>
            evaluatorsByOwner;

    public CompositeCustomerSurfaceEligibilityAuthority(
            Collection<CustomerSurfaceEligibilityRequirementBinding> bindings
    ) {
        Objects.requireNonNull(bindings, "bindings");

        Map<String, CustomerSurfaceEligibilityRequirementEvaluator> assembled =
                new HashMap<>();
        for (CustomerSurfaceEligibilityRequirementBinding binding : bindings) {
            Objects.requireNonNull(binding, "binding");
            CustomerSurfaceEligibilityRequirementEvaluator previous =
                    assembled.putIfAbsent(
                            binding.ownerIdentifier(),
                            binding.evaluator()
                    );
            if (previous != null) {
                throw new IllegalArgumentException(
                        "Duplicate customer eligibility binding for owner: "
                                + binding.ownerIdentifier()
                );
            }
        }

        this.evaluatorsByOwner = Map.copyOf(assembled);
    }

    @Override
    public Optional<Boolean> currentEligibility(
            MerchantScope merchantScope,
            TrustedExecutionContext trustedExecutionContext,
            CustomerSurfaceEligibilityRequirementIdentity requirementIdentity,
            Optional<String> requestContext
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        Objects.requireNonNull(trustedExecutionContext, "trustedExecutionContext");
        Objects.requireNonNull(requirementIdentity, "requirementIdentity");
        requestContext = Objects.requireNonNull(requestContext, "requestContext");

        if (!merchantScope.equals(trustedExecutionContext.merchantScope())) {
            return Optional.empty();
        }

        CustomerSurfaceEligibilityRequirementEvaluator evaluator =
                evaluatorsByOwner.get(requirementIdentity.ownerIdentifier());
        if (evaluator == null) {
            return Optional.empty();
        }

        Optional<Boolean> decision = evaluator.currentEligibility(
                merchantScope,
                trustedExecutionContext,
                requirementIdentity.requirementIdentifier(),
                requestContext
        );
        return decision == null ? Optional.empty() : decision;
    }
}
