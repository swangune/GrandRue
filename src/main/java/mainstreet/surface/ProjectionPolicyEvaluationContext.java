package mainstreet.surface;

import java.util.Objects;

/** Immutable context supplied to one exact registered policy evaluator. */
public record ProjectionPolicyEvaluationContext(
        ProjectionContractDefinition contract,
        ProjectionReadUseContract readUseContract,
        ProjectionServiceabilityEvaluationRequest request,
        ProjectionPolicyCategory policyCategory,
        ProjectionPolicyReference policyReference
) {
    public ProjectionPolicyEvaluationContext {
        contract = Objects.requireNonNull(contract, "contract");
        readUseContract = Objects.requireNonNull(
                readUseContract,
                "readUseContract"
        );
        request = Objects.requireNonNull(request, "request");
        policyCategory = Objects.requireNonNull(
                policyCategory,
                "policyCategory"
        );
        policyReference = Objects.requireNonNull(
                policyReference,
                "policyReference"
        );
    }
}
