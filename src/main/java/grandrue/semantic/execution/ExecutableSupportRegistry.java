package grandrue.semantic.execution;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Immutable ADR-012 support registry for the executable paths available to one
 * serving composition.
 *
 * <p>It resolves only implementation support. Semantic applicability,
 * commercial entitlement, actor authorisation, provider readiness and
 * operational eligibility remain outside this registry.</p>
 */
public final class ExecutableSupportRegistry {

    private final Map<String, ExecutableSupportManifest> manifestsByPath;

    public ExecutableSupportRegistry(
            Collection<ExecutableSupportManifest> manifests
    ) {
        Objects.requireNonNull(manifests, "manifests");
        LinkedHashMap<String, ExecutableSupportManifest> indexed =
                new LinkedHashMap<>();
        for (ExecutableSupportManifest manifest : manifests) {
            Objects.requireNonNull(manifest, "manifest");
            if (indexed.putIfAbsent(
                    manifest.implementationPathIdentifier(),
                    manifest
            ) != null) {
                throw new IllegalArgumentException(
                        "Duplicate executable implementation path: "
                                + manifest.implementationPathIdentifier()
                );
            }
        }
        manifestsByPath = Map.copyOf(indexed);
    }

    /**
     * Returns every proven path for the exact requirement in deterministic
     * path-identifier order. No newest/current-handler fallback is performed.
     */
    public List<ExecutableSupportManifest> eligiblePaths(
            ExecutableSupportRequirement requirement
    ) {
        Objects.requireNonNull(requirement, "requirement");
        return manifestsByPath.values().stream()
                .filter(manifest -> manifest.supports(requirement))
                .sorted(java.util.Comparator.comparing(
                        ExecutableSupportManifest::implementationPathIdentifier
                ))
                .toList();
    }

    /**
     * Admits one invocation to a named path only when that path proves the
     * complete exact requirement, including participant/effect contracts.
     */
    public ExecutableSupportManifest requirePath(
            String implementationPathIdentifier,
            ExecutableSupportRequirement requirement
    ) {
        if (implementationPathIdentifier == null
                || implementationPathIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Implementation path identifier must not be blank"
            );
        }
        Objects.requireNonNull(requirement, "requirement");
        ExecutableSupportManifest manifest = manifestsByPath.get(
                implementationPathIdentifier
        );
        if (manifest == null) {
            throw new IllegalArgumentException(
                    "Unknown executable implementation path: "
                            + implementationPathIdentifier
            );
        }
        if (!manifest.supports(requirement)) {
            throw new UnsupportedExecutableSupportException(
                    implementationPathIdentifier,
                    requirement
            );
        }
        return manifest;
    }

    /** Returns only the still-required contracts with no proven path. */
    public Set<ExecutableSupportRequirement> uncoveredRequirements(
            Collection<ExecutableSupportRequirement> stillRequiredRequirements
    ) {
        Objects.requireNonNull(
                stillRequiredRequirements,
                "stillRequiredRequirements"
        );
        LinkedHashSet<ExecutableSupportRequirement> uncovered =
                new LinkedHashSet<>();
        for (ExecutableSupportRequirement requirement : stillRequiredRequirements) {
            Objects.requireNonNull(requirement, "requirement");
            if (eligiblePaths(requirement).isEmpty()) {
                uncovered.add(requirement);
            }
        }
        return Set.copyOf(uncovered);
    }

    /**
     * Fails a deployment/admission plan that would knowingly orphan a
     * still-required semantic execution contract.
     */
    public void requireDeploymentCoverage(
            Collection<ExecutableSupportRequirement> stillRequiredRequirements
    ) {
        Set<ExecutableSupportRequirement> uncovered = uncoveredRequirements(
                stillRequiredRequirements
        );
        if (!uncovered.isEmpty()) {
            throw new ExecutableSupportCoverageException(uncovered);
        }
    }
}
