package mainstreet.background;

import java.util.Objects;
import java.util.Set;

/**
 * Static MS-PROT-065 v1.1 contract responsibilities. References retain explicit owner meaning;
 * construction/registration neither evaluates them nor authorizes a future mutation.
 * Even a non-consequential target must state its downstream identity rule explicitly.
 */
public record BackgroundWorkContractDefinition(
        BackgroundWorkContractIdentity identity,
        String purposeReference,
        BackgroundExecutionScope authorityScope,
        String triggerFormReference,
        BackgroundWorkTargetReference target,
        String executionPrincipalContractReference,
        String dueConditionReference,
        OverdueHandling overdueHandling,
        Set<String> currentRevalidationReferences,
        String downstreamLogicalIntentRuleReference,
        String retryContractReference,
        String uncertaintyContractReference,
        String supersessionCancellationRuleReference,
        String dataMinimisationContractReference,
        String semanticConfigurationAffinityReference
) {
    public BackgroundWorkContractDefinition {
        Objects.requireNonNull(identity, "identity");
        Objects.requireNonNull(authorityScope, "authorityScope");
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(overdueHandling, "overdueHandling");
        BackgroundWorkContractIdentity.requireReference(purposeReference, "purposeReference");
        BackgroundWorkContractIdentity.requireReference(triggerFormReference, "triggerFormReference");
        BackgroundWorkContractIdentity.requireReference(executionPrincipalContractReference, "executionPrincipalContractReference");
        BackgroundWorkContractIdentity.requireReference(dueConditionReference, "dueConditionReference");
        BackgroundWorkContractIdentity.requireReference(downstreamLogicalIntentRuleReference, "downstreamLogicalIntentRuleReference");
        BackgroundWorkContractIdentity.requireReference(retryContractReference, "retryContractReference");
        BackgroundWorkContractIdentity.requireReference(uncertaintyContractReference, "uncertaintyContractReference");
        BackgroundWorkContractIdentity.requireReference(supersessionCancellationRuleReference, "supersessionCancellationRuleReference");
        BackgroundWorkContractIdentity.requireReference(dataMinimisationContractReference, "dataMinimisationContractReference");
        BackgroundWorkContractIdentity.requireReference(semanticConfigurationAffinityReference, "semanticConfigurationAffinityReference");
        currentRevalidationReferences = Set.copyOf(Objects.requireNonNull(currentRevalidationReferences));
        if (currentRevalidationReferences.isEmpty())
            throw new IllegalArgumentException("Current revalidation requirements must be explicit");
        currentRevalidationReferences.forEach(value -> BackgroundWorkContractIdentity.requireReference(value, "current revalidation"));
    }
}
