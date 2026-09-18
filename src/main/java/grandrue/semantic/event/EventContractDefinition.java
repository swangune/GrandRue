package grandrue.semantic.event;

import java.util.Objects;
import java.util.Set;

/**
 * Static owner-declared Event Contract responsibilities under MS-PROT-026 v1.1.
 * References declare semantics; registration does not validate a payload, publish a fact or authorize a reaction.
 */
public record EventContractDefinition(
        EventContractIdentity identity,
        String factMeaningReference,
        String authorityScopeReference,
        String subjectReferenceContractReference,
        Set<String> requiredProvenanceReferences,
        String payloadContractReference,
        String compatibilityEvolutionReference
) {
    public EventContractDefinition {
        Objects.requireNonNull(identity, "identity");
        EventContractIdentity.requireReference(factMeaningReference, "factMeaningReference");
        EventContractIdentity.requireReference(authorityScopeReference, "authorityScopeReference");
        EventContractIdentity.requireReference(subjectReferenceContractReference, "subjectReferenceContractReference");
        EventContractIdentity.requireReference(payloadContractReference, "payloadContractReference");
        EventContractIdentity.requireReference(compatibilityEvolutionReference, "compatibilityEvolutionReference");
        requiredProvenanceReferences = Set.copyOf(Objects.requireNonNull(
                requiredProvenanceReferences, "requiredProvenanceReferences"));
        if (requiredProvenanceReferences.isEmpty()) {
            throw new IllegalArgumentException("Provenance requirements must be explicit");
        }
        requiredProvenanceReferences.forEach(reference ->
                EventContractIdentity.requireReference(reference, "required provenance reference"));
    }
}
