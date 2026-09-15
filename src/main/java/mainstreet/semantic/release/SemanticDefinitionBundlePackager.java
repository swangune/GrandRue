package mainstreet.semantic.release;

import java.util.Objects;

/**
 * Deterministic trusted-build packaging boundary from accepted Published
 * Semantic Definition evidence to one deployable semantic definition bundle.
 *
 * <p>The packager does not invent or rewrite release identity, provenance or
 * missing definition constituents. Complete v2 packaging requires explicit
 * Exposure definition evidence.</p>
 */
public final class SemanticDefinitionBundlePackager {

    public PackagedSemanticDefinitionBundle packageEvidence(
            PublishedSemanticDefinitionSet evidence
    ) {
        Objects.requireNonNull(evidence, "evidence");
        if (!evidence.hasExposureDefinitions()) {
            throw new IllegalArgumentException(
                    "Semantic bundle v2 packaging requires explicit Exposure definition evidence"
            );
        }

        byte[] semantic = evidence.semanticDefinitions();
        byte[] surface = evidence.surfaceDefinitions();
        byte[] fulfilment = evidence.fulfilmentDefinitions();
        byte[] exposure = evidence.exposureDefinitions();
        String digest = PackagedSemanticDefinitionBundle.computeDigest(
                PackagedSemanticDefinitionBundle.CURRENT_FORMAT,
                evidence.releaseIdentifier(),
                evidence.publicationProvenance(),
                semantic,
                surface,
                fulfilment,
                exposure
        );

        return new PackagedSemanticDefinitionBundle(
                PackagedSemanticDefinitionBundle.CURRENT_FORMAT,
                evidence.releaseIdentifier(),
                evidence.publicationProvenance(),
                digest,
                semantic,
                surface,
                fulfilment,
                exposure
        );
    }
}
