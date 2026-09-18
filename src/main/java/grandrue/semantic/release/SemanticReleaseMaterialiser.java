package grandrue.semantic.release;

import grandrue.fulfilment.FulfilmentContractRegistrySnapshot;
import grandrue.semantic.registry.SemanticRegistrySnapshot;
import mainstreet.surface.ExposureElementContractRegistrySnapshot;
import mainstreet.surface.SurfaceContributionRegistrySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Deterministically materialises an immutable exact-release repository from the
 * complete deployment materialisation set.
 *
 * <p>No repository is returned until every bundle has passed envelope
 * validation, registry-owned decoding and release-affinity validation. This
 * gives startup all-or-nothing publication without making materialisation
 * executable-support or Exposure-decision authority.</p>
 */
public final class SemanticReleaseMaterialiser {

    private final SemanticDefinitionSectionDecoder<SemanticRegistrySnapshot>
            semanticDecoder;
    private final SemanticDefinitionSectionDecoder<SurfaceContributionRegistrySnapshot>
            surfaceDecoder;
    private final SemanticDefinitionSectionDecoder<FulfilmentContractRegistrySnapshot>
            fulfilmentDecoder;
    private final SemanticDefinitionSectionDecoder<ExposureElementContractRegistrySnapshot>
            exposureDecoder;

    public SemanticReleaseMaterialiser(
            SemanticDefinitionSectionDecoder<SemanticRegistrySnapshot> semanticDecoder,
            SemanticDefinitionSectionDecoder<SurfaceContributionRegistrySnapshot> surfaceDecoder,
            SemanticDefinitionSectionDecoder<FulfilmentContractRegistrySnapshot> fulfilmentDecoder,
            SemanticDefinitionSectionDecoder<ExposureElementContractRegistrySnapshot> exposureDecoder
    ) {
        this.semanticDecoder = Objects.requireNonNull(
                semanticDecoder,
                "semanticDecoder"
        );
        this.surfaceDecoder = Objects.requireNonNull(
                surfaceDecoder,
                "surfaceDecoder"
        );
        this.fulfilmentDecoder = Objects.requireNonNull(
                fulfilmentDecoder,
                "fulfilmentDecoder"
        );
        this.exposureDecoder = Objects.requireNonNull(
                exposureDecoder,
                "exposureDecoder"
        );
    }

    public SemanticReleaseAssemblyRepository materialise(
            DeploymentSemanticMaterialisationSet deploymentSet
    ) {
        Objects.requireNonNull(deploymentSet, "deploymentSet");
        List<SemanticReleaseAssembly> materialised = new ArrayList<>();

        for (PackagedSemanticDefinitionBundle bundle : deploymentSet.bundles()) {
            validateEnvelope(bundle);

            SemanticRegistrySnapshot semantic = decode(
                    semanticDecoder,
                    bundle.semanticDefinitions(),
                    bundle.releaseIdentifier(),
                    "semantic registry"
            );
            SurfaceContributionRegistrySnapshot surface = decode(
                    surfaceDecoder,
                    bundle.surfaceDefinitions(),
                    bundle.releaseIdentifier(),
                    "surface registry"
            );
            FulfilmentContractRegistrySnapshot fulfilment = decode(
                    fulfilmentDecoder,
                    bundle.fulfilmentDefinitions(),
                    bundle.releaseIdentifier(),
                    "fulfilment registry"
            );
            ExposureElementContractRegistrySnapshot exposure = decode(
                    exposureDecoder,
                    bundle.exposureDefinitions(),
                    bundle.releaseIdentifier(),
                    "Exposure registry"
            );

            requireExactRelease(
                    bundle.releaseIdentifier(),
                    semantic.version(),
                    "semantic registry"
            );
            requireExactRelease(
                    bundle.releaseIdentifier(),
                    surface.semanticRegistryReleaseIdentifier(),
                    "surface registry"
            );
            requireExactRelease(
                    bundle.releaseIdentifier(),
                    fulfilment.semanticRegistryReleaseIdentifier(),
                    "fulfilment registry"
            );
            requireExactRelease(
                    bundle.releaseIdentifier(),
                    exposure.semanticRegistryReleaseIdentifier(),
                    "Exposure registry"
            );

            try {
                materialised.add(new SemanticReleaseAssembly(
                        semantic,
                        surface,
                        fulfilment,
                        exposure
                ));
            } catch (RuntimeException exception) {
                throw new SemanticMaterialisationException(
                        SemanticMaterialisationFailure.ASSEMBLY_COHERENCE_FAILURE,
                        "Semantic release assembly is incoherent for release: "
                                + bundle.releaseIdentifier(),
                        exception
                );
            }
        }

        return new InMemorySemanticReleaseAssemblyRepository(materialised);
    }

    private static void validateEnvelope(PackagedSemanticDefinitionBundle bundle) {
        Objects.requireNonNull(bundle, "semantic definition bundle");
        if (!PackagedSemanticDefinitionBundle.CURRENT_FORMAT.equals(
                bundle.formatVersion()
        )) {
            throw new SemanticMaterialisationException(
                    SemanticMaterialisationFailure.UNSUPPORTED_BUNDLE_FORMAT,
                    "Unsupported semantic definition bundle format: "
                            + bundle.formatVersion()
            );
        }
        if (!bundle.hasExposureDefinitions()) {
            throw new SemanticMaterialisationException(
                    SemanticMaterialisationFailure.UNSUPPORTED_BUNDLE_FORMAT,
                    "Semantic definition bundle v2 lacks Exposure definitions: "
                            + bundle.releaseIdentifier()
            );
        }
        if (!bundle.integrityVerified()) {
            throw new SemanticMaterialisationException(
                    SemanticMaterialisationFailure.CONTENT_INTEGRITY_FAILURE,
                    "Semantic definition bundle failed integrity verification: "
                            + bundle.releaseIdentifier()
            );
        }
    }

    private static <T> T decode(
            SemanticDefinitionSectionDecoder<T> decoder,
            byte[] payload,
            String releaseIdentifier,
            String section
    ) {
        try {
            T decoded = decoder.decode(payload);
            if (decoded == null) {
                throw new IllegalArgumentException("decoder returned null");
            }
            return decoded;
        } catch (RuntimeException exception) {
            throw new SemanticMaterialisationException(
                    SemanticMaterialisationFailure.DEFINITION_DECODE_FAILURE,
                    "Unable to decode " + section + " for semantic release: "
                            + releaseIdentifier,
                    exception
            );
        }
    }

    private static void requireExactRelease(
            String bundleRelease,
            String decodedRelease,
            String section
    ) {
        if (!bundleRelease.equals(decodedRelease)) {
            throw new SemanticMaterialisationException(
                    SemanticMaterialisationFailure.RELEASE_IDENTITY_MISMATCH,
                    "Decoded " + section + " release does not match packaged release: "
                            + bundleRelease + " != " + decodedRelease
            );
        }
    }
}
