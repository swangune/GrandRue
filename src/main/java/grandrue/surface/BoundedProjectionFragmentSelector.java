package grandrue.surface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Selects immutable Projection material for one already-resolved API Exposure
 * result without re-running Projection Serviceability or Exposure evaluation.
 */
final class BoundedProjectionFragmentSelector {

    List<ProjectionMaterialFragment> select(
            BoundedProjectionRead boundedRead,
            ProjectionServiceabilityResult serviceability,
            ApiExposureResolution exposureResolution
    ) {
        Objects.requireNonNull(boundedRead, "boundedRead");
        Objects.requireNonNull(serviceability, "serviceability");
        Objects.requireNonNull(exposureResolution, "exposureResolution");

        validateServiceabilityAffinity(boundedRead, serviceability);
        validateExposureAffinity(boundedRead, exposureResolution);

        Map<ExposureCandidateObservation, ProjectionMaterialFragment>
                eligibleFragments = eligibleFragments(
                        boundedRead,
                        serviceability
                );
        Set<ExposureCandidateObservation> positiveCandidates =
                new HashSet<>();

        for (ExposedElementMembership membership
                : exposureResolution.exposedElements().members()) {
            ExposureCandidateObservation candidate =
                    new ExposureCandidateObservation(
                            membership.elementReference(),
                            membership.memberInstanceReference()
                    );
            if (!positiveCandidates.add(candidate)) {
                throw structural(
                        "Duplicate positive Exposure membership identity"
                );
            }
            if (!eligibleFragments.containsKey(candidate)) {
                throw structural(
                        "Positive Exposure membership does not map to exactly "
                                + "one P2-eligible fragment in the same bounded read"
                );
            }
        }

        List<ProjectionMaterialFragment> selected = new ArrayList<>();
        for (ProjectionMaterialFragment fragment : boundedRead.fragments()) {
            if (positiveCandidates.contains(fragment.candidateObservation())) {
                selected.add(fragment);
            }
        }
        if (selected.size() != positiveCandidates.size()) {
            throw structural(
                    "Positive Exposure membership selection is structurally incomplete"
            );
        }
        return List.copyOf(selected);
    }

    private static void validateServiceabilityAffinity(
            BoundedProjectionRead boundedRead,
            ProjectionServiceabilityResult serviceability
    ) {
        if (!serviceability.hasExactBoundedReadAffinity(boundedRead)
                || !serviceability.semanticRegistryReleaseIdentifier().equals(
                        boundedRead.semanticRegistryReleaseIdentifier()
                )
                || !serviceability.contractIdentity().equals(
                        boundedRead.contractIdentity()
                )
                || !serviceability.readUseIdentity().equals(
                        boundedRead.readUseIdentity()
                )
                || !serviceability.sourceEvidence().equals(
                        boundedRead.sourceEvidence()
                )) {
            throw structural(
                    "Projection Serviceability result does not belong to the exact bounded read"
            );
        }
        if (serviceability.outcome()
                == ProjectionServiceabilityOutcome.NOT_SERVICEABLE) {
            throw structural(
                    "A non-serviceable bounded read cannot supply representation material"
            );
        }
    }

    private static void validateExposureAffinity(
            BoundedProjectionRead boundedRead,
            ApiExposureResolution exposureResolution
    ) {
        if (ApiExposureResolutionDetails.requestBinding(exposureResolution)
                != boundedRead.requestBinding()) {
            throw structural(
                    "API Exposure resolution belongs to another observation request"
            );
        }
        if (!ApiExposureResolutionDetails
                .semanticRegistryReleaseIdentifier(exposureResolution)
                .equals(boundedRead.semanticRegistryReleaseIdentifier())) {
            throw structural(
                    "API Exposure resolution belongs to another semantic registry release"
            );
        }
    }

    private static Map<ExposureCandidateObservation, ProjectionMaterialFragment>
            eligibleFragments(
                    BoundedProjectionRead boundedRead,
                    ProjectionServiceabilityResult serviceability
            ) {
        Map<ProjectionSourceDependencyReference, ProjectionSourceEvidence>
                evidenceBySource = new HashMap<>();
        for (ProjectionSourceEvidence evidence : boundedRead.sourceEvidence()) {
            ProjectionSourceEvidence previous = evidenceBySource.put(
                    evidence.sourceReference(),
                    evidence
            );
            if (previous != null) {
                throw structural(
                        "Bounded read contains competing source evidence"
                );
            }
        }

        Map<ExposureCandidateObservation, ProjectionMaterialFragment> eligible =
                new HashMap<>();
        for (ProjectionMaterialFragment fragment : boundedRead.fragments()) {
            if (!isP2Eligible(
                    fragment,
                    evidenceBySource,
                    serviceability.omittedSourceReferences()
            )) {
                continue;
            }
            ProjectionMaterialFragment previous = eligible.put(
                    fragment.candidateObservation(),
                    fragment
            );
            if (previous != null) {
                throw structural(
                        "Multiple P2-eligible fragments compete for one candidate identity"
                );
            }
        }
        return Map.copyOf(eligible);
    }

    private static boolean isP2Eligible(
            ProjectionMaterialFragment fragment,
            Map<ProjectionSourceDependencyReference, ProjectionSourceEvidence>
                    evidenceBySource,
            Set<ProjectionSourceDependencyReference> omittedSources
    ) {
        for (ProjectionMaterialSourceAffinity affinity
                : fragment.sourceAffinities()) {
            if (omittedSources.contains(affinity.sourceReference())) {
                return false;
            }
            ProjectionSourceEvidence evidence =
                    evidenceBySource.get(affinity.sourceReference());
            if (evidence == null || evidence.isNotApplicable()) {
                return false;
            }
            Optional<String> observed = evidence.observedProgressIdentifier();
            if (observed.isEmpty()
                    || !observed.orElseThrow().equals(
                            affinity.observedProgressIdentifier()
                    )) {
                return false;
            }
        }
        return true;
    }

    private static ProjectionFragmentSelectionStructuralException structural(
            String message
    ) {
        return new ProjectionFragmentSelectionStructuralException(message);
    }
}

/** Structural same-read/affinity failure; never a legitimate WITHHOLD outcome. */
final class ProjectionFragmentSelectionStructuralException
        extends RuntimeException {

    ProjectionFragmentSelectionStructuralException(String message) {
        super(message);
    }
}
