package grandrue.surface;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Immutable Exposure Element Contract registry for one exact Semantic Registry
 * Release. Lookup deliberately has no latest-release fallback.
 */
public final class ExposureElementContractRegistrySnapshot {

    private final String semanticRegistryReleaseIdentifier;
    private final Map<ExposureElementContractIdentity, ExposureElementContract>
            contracts;
    private final Map<ExposableElementReference,
            Map<SurfaceAudience, ExposureElementContract>> contractsByElementAudience;

    public ExposureElementContractRegistrySnapshot(
            String semanticRegistryReleaseIdentifier,
            Set<ExposureElementContract> contracts
    ) {
        requireIdentifier(
                semanticRegistryReleaseIdentifier,
                "Semantic registry release identifier"
        );
        this.semanticRegistryReleaseIdentifier =
                semanticRegistryReleaseIdentifier;
        RegistryIndexes indexes = index(
                Objects.requireNonNull(contracts, "contracts")
        );
        this.contracts = indexes.byIdentity();
        this.contractsByElementAudience = indexes.byElementAudience();
    }

    public String semanticRegistryReleaseIdentifier() {
        return semanticRegistryReleaseIdentifier;
    }

    public Set<ExposureElementContract> contracts() {
        return Set.copyOf(contracts.values());
    }

    /**
     * Resolves only within the explicitly requested release. A release mismatch
     * or absent identity remains unresolved for fail-closed caller handling.
     */
    public Optional<ExposureElementContract> contract(
            String requestedSemanticRegistryReleaseIdentifier,
            ExposureElementContractIdentity identity
    ) {
        requireIdentifier(
                requestedSemanticRegistryReleaseIdentifier,
                "Requested semantic registry release identifier"
        );
        Objects.requireNonNull(identity, "identity");
        if (!matchesRelease(requestedSemanticRegistryReleaseIdentifier)) {
            return Optional.empty();
        }
        return Optional.ofNullable(contracts.get(identity));
    }

    /**
     * Resolves the unique contract variant for an exact element and audience
     * within the explicitly requested release. The caller does not need to know
     * the contract identifier.
     */
    public Optional<ExposureElementContract> contract(
            String requestedSemanticRegistryReleaseIdentifier,
            ExposableElementReference elementReference,
            SurfaceAudience audience
    ) {
        requireIdentifier(
                requestedSemanticRegistryReleaseIdentifier,
                "Requested semantic registry release identifier"
        );
        Objects.requireNonNull(elementReference, "elementReference");
        Objects.requireNonNull(audience, "audience");
        if (!matchesRelease(requestedSemanticRegistryReleaseIdentifier)) {
            return Optional.empty();
        }
        Map<SurfaceAudience, ExposureElementContract> variants =
                contractsByElementAudience.get(elementReference);
        if (variants == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(variants.get(audience));
    }

    /**
     * Distinguishes an unknown element from a known element that simply has no
     * contract for a requested audience.
     */
    public boolean containsElement(
            String requestedSemanticRegistryReleaseIdentifier,
            ExposableElementReference elementReference
    ) {
        requireIdentifier(
                requestedSemanticRegistryReleaseIdentifier,
                "Requested semantic registry release identifier"
        );
        Objects.requireNonNull(elementReference, "elementReference");
        return matchesRelease(requestedSemanticRegistryReleaseIdentifier)
                && contractsByElementAudience.containsKey(elementReference);
    }

    /** Returns the immutable set of audiences defined for one exact element. */
    public Set<SurfaceAudience> audiences(
            String requestedSemanticRegistryReleaseIdentifier,
            ExposableElementReference elementReference
    ) {
        requireIdentifier(
                requestedSemanticRegistryReleaseIdentifier,
                "Requested semantic registry release identifier"
        );
        Objects.requireNonNull(elementReference, "elementReference");
        if (!matchesRelease(requestedSemanticRegistryReleaseIdentifier)) {
            return Set.of();
        }
        Map<SurfaceAudience, ExposureElementContract> variants =
                contractsByElementAudience.get(elementReference);
        return variants == null ? Set.of() : Set.copyOf(variants.keySet());
    }

    private boolean matchesRelease(String requestedReleaseIdentifier) {
        return semanticRegistryReleaseIdentifier.equals(requestedReleaseIdentifier);
    }

    private static RegistryIndexes index(Set<ExposureElementContract> definitions) {
        Map<ExposureElementContractIdentity, ExposureElementContract> byIdentity =
                new HashMap<>();
        Map<ExposableElementReference,
                Map<SurfaceAudience, ExposureElementContract>> byElementAudience =
                new HashMap<>();
        Map<ExposableElementReference, ExposureMemberIdentitySpecification>
                memberIdentityByElement = new HashMap<>();

        for (ExposureElementContract definition : definitions) {
            Objects.requireNonNull(definition, "exposure element contract");
            if (byIdentity.putIfAbsent(
                    definition.identity(),
                    definition
            ) != null) {
                throw new IllegalArgumentException(
                        "Duplicate Exposure Element Contract identity: "
                                + definition.identity()
                );
            }

            ExposureMemberIdentitySpecification existingSpecification =
                    memberIdentityByElement.putIfAbsent(
                            definition.exposableElementReference(),
                            definition.memberIdentitySpecification()
                    );
            if (existingSpecification != null
                    && !existingSpecification.equals(
                            definition.memberIdentitySpecification()
                    )) {
                throw new IllegalArgumentException(
                        "Exposure audience variants for one element must share "
                                + "member identity semantics: "
                                + definition.exposableElementReference()
                );
            }

            Map<SurfaceAudience, ExposureElementContract> variants =
                    byElementAudience.computeIfAbsent(
                            definition.exposableElementReference(),
                            ignored -> new EnumMap<>(SurfaceAudience.class)
                    );
            if (variants.putIfAbsent(definition.audience(), definition) != null) {
                throw new IllegalArgumentException(
                        "Duplicate Exposure Element Contract element/audience variant: "
                                + definition.exposableElementReference()
                                + " / "
                                + definition.audience()
                );
            }
        }

        Map<ExposableElementReference,
                Map<SurfaceAudience, ExposureElementContract>> immutableElementIndex =
                new HashMap<>();
        byElementAudience.forEach((element, variants) ->
                immutableElementIndex.put(element, Map.copyOf(variants))
        );

        return new RegistryIndexes(
                Map.copyOf(byIdentity),
                Map.copyOf(immutableElementIndex)
        );
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }

    private record RegistryIndexes(
            Map<ExposureElementContractIdentity, ExposureElementContract> byIdentity,
            Map<ExposableElementReference,
                    Map<SurfaceAudience, ExposureElementContract>> byElementAudience
    ) {
    }
}
