package grandrue.semantic.release;

import grandrue.fulfilment.FulfilmentContractRegistrySnapshot;
import grandrue.semantic.registry.SemanticRegistrySnapshot;
import grandrue.surface.ExposureElementContractRegistrySnapshot;
import grandrue.surface.SurfaceContributionRegistrySnapshot;

import java.util.Objects;

/**
 * Immutable assembly of the static authorities that belong to one semantic
 * registry release.
 *
 * <p>The assembly owns release coherence only. Constituent registries retain
 * their existing semantic and technical ownership.</p>
 */
public record SemanticReleaseAssembly(
        SemanticRegistrySnapshot semanticRegistry,
        SurfaceContributionRegistrySnapshot surfaceRegistry,
        FulfilmentContractRegistrySnapshot fulfilmentRegistry,
        ExposureElementContractRegistrySnapshot exposureRegistry
) {

    public SemanticReleaseAssembly {
        semanticRegistry = Objects.requireNonNull(
                semanticRegistry,
                "semanticRegistry"
        );
        surfaceRegistry = Objects.requireNonNull(
                surfaceRegistry,
                "surfaceRegistry"
        );
        fulfilmentRegistry = Objects.requireNonNull(
                fulfilmentRegistry,
                "fulfilmentRegistry"
        );
        exposureRegistry = Objects.requireNonNull(
                exposureRegistry,
                "exposureRegistry"
        );

        String releaseIdentifier = semanticRegistry.version();
        if (!releaseIdentifier.equals(
                surfaceRegistry.semanticRegistryReleaseIdentifier()
        )) {
            throw new IllegalArgumentException(
                    "Surface registry does not belong to semantic release: "
                            + releaseIdentifier
            );
        }
        if (!releaseIdentifier.equals(
                fulfilmentRegistry.semanticRegistryReleaseIdentifier()
        )) {
            throw new IllegalArgumentException(
                    "Fulfilment registry does not belong to semantic release: "
                            + releaseIdentifier
            );
        }
        if (!releaseIdentifier.equals(
                exposureRegistry.semanticRegistryReleaseIdentifier()
        )) {
            throw new IllegalArgumentException(
                    "Exposure registry does not belong to semantic release: "
                            + releaseIdentifier
            );
        }
    }

    /** Returns the canonical semantic registry release identifier. */
    public String releaseIdentifier() {
        return semanticRegistry.version();
    }
}
