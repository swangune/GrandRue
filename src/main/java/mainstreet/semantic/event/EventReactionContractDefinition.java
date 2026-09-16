package mainstreet.semantic.event;

import java.util.Objects;
import java.util.Set;

/**
 * Registration declares a consumer responsibility; it does not confer execution authority.
 * MS-PROT-026 v1.1 — Production Domain Event Publication, Reaction & Consumption Contract Amendment,
 * §13 — EventReactionContract; §14 — Registration Is Required.
 */
public record EventReactionContractDefinition(
        EventReactionContractIdentity identity,
        EventContractAffinity sourceEventContract,
        String purposeReference,
        String authorityScopeReference,
        String executionModeReference,
        EventReactionTargetReference target,
        String executionPrincipalContractReference,
        String duplicateIdentityRuleReference,
        String orderingRequirementReference,
        String supersessionCoalescingRuleReference,
        Set<String> requiredEventDataReferences,
        String failureRetryContractReference,
        String downstreamLogicalIntentRuleReference
) {
    public EventReactionContractDefinition {
        Objects.requireNonNull(identity, "identity");
        Objects.requireNonNull(sourceEventContract, "sourceEventContract");
        Objects.requireNonNull(target, "target");
        EventContractIdentity.requireReference(purposeReference, "purposeReference");
        EventContractIdentity.requireReference(authorityScopeReference, "authorityScopeReference");
        EventContractIdentity.requireReference(executionModeReference, "executionModeReference");
        EventContractIdentity.requireReference(executionPrincipalContractReference, "executionPrincipalContractReference");
        EventContractIdentity.requireReference(duplicateIdentityRuleReference, "duplicateIdentityRuleReference");
        EventContractIdentity.requireReference(orderingRequirementReference, "orderingRequirementReference");
        EventContractIdentity.requireReference(supersessionCoalescingRuleReference, "supersessionCoalescingRuleReference");
        EventContractIdentity.requireReference(failureRetryContractReference, "failureRetryContractReference");
        EventContractIdentity.requireReference(downstreamLogicalIntentRuleReference, "downstreamLogicalIntentRuleReference");
        requiredEventDataReferences = Set.copyOf(Objects.requireNonNull(requiredEventDataReferences, "requiredEventDataReferences"));
        if (requiredEventDataReferences.isEmpty()) {
            throw new IllegalArgumentException("Required event data must be explicit");
        }
        requiredEventDataReferences.forEach(value -> EventContractIdentity.requireReference(value, "required event data"));
    }
}
