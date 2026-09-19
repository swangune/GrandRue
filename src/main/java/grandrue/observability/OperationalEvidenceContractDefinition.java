package grandrue.observability;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Registered minimum diagnosability for one operational responsibility.
 *
 * <p>This definition grants no business mutation, retry, acknowledgement,
 * reconciliation-completion or provider authority. Authority: MS-PROT-068 v1.1
 * §§2-6, 16-22 and 29.</p>
 */
public record OperationalEvidenceContractDefinition(
        OperationalEvidenceContractIdentity identity,
        String owningResponsibilityReference,
        String observationScopeReference,
        Set<OperationalEvidenceFamily> evidenceFamilies,
        Set<OperationalCorrelationReference> safeCorrelationReferences,
        Optional<String> healthInterpretationReference,
        String freshnessInterpretationReference,
        String failureBacklogEvidenceReference,
        Optional<String> alertingOrEscalationEligibilityReference,
        String dataMinimisationReference,
        String retentionClassificationReference,
        Set<String> audienceAccessRestrictionReferences
) {
    public OperationalEvidenceContractDefinition {
        Objects.requireNonNull(identity, "identity");
        requireReference(
                owningResponsibilityReference,
                "owningResponsibilityReference"
        );
        requireReference(
                observationScopeReference,
                "observationScopeReference"
        );

        evidenceFamilies = Set.copyOf(
                Objects.requireNonNull(
                        evidenceFamilies,
                        "evidenceFamilies"
                )
        );
        if (evidenceFamilies.isEmpty()) {
            throw new IllegalArgumentException(
                    "Operational evidence families must be explicit"
            );
        }

        safeCorrelationReferences = Set.copyOf(
                Objects.requireNonNull(
                        safeCorrelationReferences,
                        "safeCorrelationReferences"
                )
        );
        if (safeCorrelationReferences.isEmpty()) {
            throw new IllegalArgumentException(
                    "Safe correlation references must be explicit"
            );
        }

        healthInterpretationReference = normalise(
                healthInterpretationReference,
                "healthInterpretationReference"
        );
        requireReference(
                freshnessInterpretationReference,
                "freshnessInterpretationReference"
        );
        requireReference(
                failureBacklogEvidenceReference,
                "failureBacklogEvidenceReference"
        );
        alertingOrEscalationEligibilityReference = normalise(
                alertingOrEscalationEligibilityReference,
                "alertingOrEscalationEligibilityReference"
        );
        requireReference(
                dataMinimisationReference,
                "dataMinimisationReference"
        );
        requireReference(
                retentionClassificationReference,
                "retentionClassificationReference"
        );

        audienceAccessRestrictionReferences = Set.copyOf(
                Objects.requireNonNull(
                        audienceAccessRestrictionReferences,
                        "audienceAccessRestrictionReferences"
                )
        );
        if (audienceAccessRestrictionReferences.isEmpty()) {
            throw new IllegalArgumentException(
                    "Operational evidence audience/access restrictions "
                            + "must be explicit"
            );
        }
        audienceAccessRestrictionReferences.forEach(value ->
                requireReference(
                        value,
                        "audienceAccessRestrictionReference"
                )
        );
    }

    private static Optional<String> normalise(
            Optional<String> value,
            String label
    ) {
        Objects.requireNonNull(value, label);
        value.ifPresent(item -> requireReference(item, label));
        return value;
    }

    private static void requireReference(
            String value,
            String label
    ) {
        OperationalEvidenceContractIdentity.requireReference(
                value,
                label
        );
    }
}
