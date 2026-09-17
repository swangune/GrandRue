package grandrue.deployment;

import mainstreet.semantic.execution.ExecutableSupportManifest;
import mainstreet.semantic.execution.ExecutableSupportRegistry;
import mainstreet.semantic.execution.ExecutableSupportRequirement;

import java.time.Instant;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/** Immutable normalized technical evidence for one exact serving generation. */
public record ServingDeploymentAdmissionSnapshot(
        String generationIdentifier,
        ServingDeploymentCohort cohort,
        Map<String, String> materialisedReleaseDigests,
        Set<ExecutableSupportManifest> supportManifests,
        Instant evidenceRecordedAt
) {
    public ServingDeploymentAdmissionSnapshot {
        requireIdentifier(generationIdentifier, "Generation identifier");
        Objects.requireNonNull(cohort, "cohort");
        if (cohort != ServingDeploymentCohort.ORDINARY) {
            throw new IllegalArgumentException(
                    "Only the ORDINARY serving cohort is governed"
            );
        }
        materialisedReleaseDigests = immutableMaterialisation(
                materialisedReleaseDigests
        );
        supportManifests = Set.copyOf(
                Objects.requireNonNull(supportManifests, "supportManifests")
        );
        new ExecutableSupportRegistry(supportManifests);
        Objects.requireNonNull(evidenceRecordedAt, "evidenceRecordedAt");
    }

    public boolean materialises(String semanticRelease, String bundleDigest) {
        requireIdentifier(semanticRelease, "Semantic release identifier");
        requireIdentifier(bundleDigest, "Bundle content digest");
        return bundleDigest.equals(materialisedReleaseDigests.get(
                semanticRelease
        ));
    }

    public Set<ExecutableSupportRequirement> uncoveredRequirements(
            Collection<ExecutableSupportRequirement> requirements
    ) {
        return new ExecutableSupportRegistry(supportManifests)
                .uncoveredRequirements(requirements);
    }

    private static Map<String, String> immutableMaterialisation(
            Map<String, String> materialisedReleaseDigests
    ) {
        Objects.requireNonNull(
                materialisedReleaseDigests,
                "materialisedReleaseDigests"
        );
        if (materialisedReleaseDigests.isEmpty()) {
            throw new IllegalArgumentException(
                    "A serving snapshot requires at least one materialised release"
            );
        }
        LinkedHashMap<String, String> copy = new LinkedHashMap<>();
        materialisedReleaseDigests.forEach((release, digest) -> {
            requireIdentifier(release, "Semantic release identifier");
            requireIdentifier(digest, "Bundle content digest");
            copy.put(release, digest);
        });
        return Map.copyOf(copy);
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
