package grandrue.surface;

import java.util.Objects;

/**
 * Immutable references to the policies governing one materially distinct
 * projection read use. It registers policy identity without evaluating it.
 */
public record ProjectionReadUseContract(
        ProjectionReadUseIdentity readUseIdentity,
        ProjectionPolicyReference serviceabilityPolicyReference,
        ProjectionPolicyReference missingEvidencePolicyReference,
        ProjectionPolicyReference staleServingPolicyReference
) {
    public ProjectionReadUseContract {
        readUseIdentity = Objects.requireNonNull(
                readUseIdentity,
                "readUseIdentity"
        );
        serviceabilityPolicyReference = Objects.requireNonNull(
                serviceabilityPolicyReference,
                "serviceabilityPolicyReference"
        );
        missingEvidencePolicyReference = Objects.requireNonNull(
                missingEvidencePolicyReference,
                "missingEvidencePolicyReference"
        );
        staleServingPolicyReference = Objects.requireNonNull(
                staleServingPolicyReference,
                "staleServingPolicyReference"
        );
    }
}
