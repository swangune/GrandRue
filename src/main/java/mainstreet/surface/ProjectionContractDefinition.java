package mainstreet.surface;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Immutable exact-release Projection Contract definition. Policy references
 * describe required authority; current evidence and evaluation remain outside
 * this static definition.
 */
public record ProjectionContractDefinition(
        ProjectionContractIdentity identity,
        Set<ProjectionContractApplicabilityTrigger> applicabilityTriggers,
        Set<ProjectionSourceDependencyReference> authoritativeSourceReferences,
        ProjectionMaterialisationKind materialisationKind,
        ProjectionPolicyReference freshnessPolicyReference,
        Set<ProjectionReadUseContract> readUseContracts,
        Optional<ProjectionPolicyReference> revocationPolicyReference,
        Optional<ProjectionPolicyReference> rebuildPolicyReference
) {
    public ProjectionContractDefinition {
        identity = Objects.requireNonNull(identity, "identity");
        applicabilityTriggers = immutableNonEmpty(
                applicabilityTriggers,
                "applicabilityTriggers"
        );
        authoritativeSourceReferences = immutableNonEmpty(
                authoritativeSourceReferences,
                "authoritativeSourceReferences"
        );
        materialisationKind = Objects.requireNonNull(
                materialisationKind,
                "materialisationKind"
        );
        freshnessPolicyReference = Objects.requireNonNull(
                freshnessPolicyReference,
                "freshnessPolicyReference"
        );
        readUseContracts = immutableNonEmpty(
                readUseContracts,
                "readUseContracts"
        );
        revocationPolicyReference = Objects.requireNonNull(
                revocationPolicyReference,
                "revocationPolicyReference"
        );
        rebuildPolicyReference = Objects.requireNonNull(
                rebuildPolicyReference,
                "rebuildPolicyReference"
        );
        requireUniqueReadUseIdentities(readUseContracts);
        requireMaterialisedContractEvidence(
                materialisationKind,
                applicabilityTriggers,
                rebuildPolicyReference
        );
    }

    private static <T> Set<T> immutableNonEmpty(
            Set<T> values,
            String label
    ) {
        Set<T> copy = Set.copyOf(Objects.requireNonNull(values, label));
        if (copy.isEmpty()) {
            throw new IllegalArgumentException(label + " must not be empty");
        }
        return copy;
    }

    private static void requireUniqueReadUseIdentities(
            Set<ProjectionReadUseContract> contracts
    ) {
        Set<ProjectionReadUseIdentity> identities = new HashSet<>();
        for (ProjectionReadUseContract contract : contracts) {
            if (!identities.add(contract.readUseIdentity())) {
                throw new IllegalArgumentException(
                        "Duplicate projection read-use identity: "
                                + contract.readUseIdentity()
                );
            }
        }
    }

    private static void requireMaterialisedContractEvidence(
            ProjectionMaterialisationKind materialisationKind,
            Set<ProjectionContractApplicabilityTrigger> triggers,
            Optional<ProjectionPolicyReference> rebuildPolicyReference
    ) {
        if (materialisationKind != ProjectionMaterialisationKind.MATERIALISED) {
            return;
        }
        if (!triggers.contains(
                ProjectionContractApplicabilityTrigger.PERSISTED_OR_CACHED
        )) {
            throw new IllegalArgumentException(
                    "Materialised projection contract must declare "
                            + "PERSISTED_OR_CACHED"
            );
        }
        if (rebuildPolicyReference.isEmpty()) {
            throw new IllegalArgumentException(
                    "Materialised projection contract must declare a rebuild "
                            + "policy reference"
            );
        }
    }
}
