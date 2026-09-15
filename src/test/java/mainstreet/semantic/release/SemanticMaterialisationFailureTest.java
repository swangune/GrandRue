package mainstreet.semantic.release;

import mainstreet.fulfilment.FulfilmentContractRegistrySnapshot;
import mainstreet.semantic.registry.SemanticRegistrySnapshot;
import mainstreet.surface.ExposureElementContractRegistrySnapshot;
import mainstreet.surface.SurfaceContributionRegistrySnapshot;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SemanticMaterialisationFailureTest {

    @Test
    void unsupported_bundle_format_is_distinguishable() {
        PackagedSemanticDefinitionBundle bundle = legacyBundle(
                "unsupported-v99",
                "release-a",
                "semantic:release-a",
                "surface:release-a",
                "fulfilment:release-a"
        );

        SemanticMaterialisationException failure = assertThrows(
                SemanticMaterialisationException.class,
                () -> materialiser().materialise(
                        new DeploymentSemanticMaterialisationSet(List.of(bundle))
                )
        );

        assertEquals(
                SemanticMaterialisationFailure.UNSUPPORTED_BUNDLE_FORMAT,
                failure.failure()
        );
    }

    @Test
    void content_integrity_failure_is_distinguishable() {
        PackagedSemanticDefinitionBundle valid = bundleFor("release-a");

        SemanticMaterialisationException failure = assertThrows(
                SemanticMaterialisationException.class,
                () -> new PackagedSemanticDefinitionBundle(
                        valid.formatVersion(),
                        valid.releaseIdentifier(),
                        valid.publicationProvenance(),
                        valid.contentDigest(),
                        bytes("semantic:tampered"),
                        valid.surfaceDefinitions(),
                        valid.fulfilmentDefinitions(),
                        valid.exposureDefinitions()
                )
        );

        assertEquals(
                SemanticMaterialisationFailure.CONTENT_INTEGRITY_FAILURE,
                failure.failure()
        );
    }

    @Test
    void reconstructed_release_identity_mismatch_is_distinguishable() {
        SemanticReleaseMaterialiser materialiser = new SemanticReleaseMaterialiser(
                payload -> semantic("release-other"),
                payload -> surface(releaseFrom(payload)),
                payload -> fulfilment(releaseFrom(payload)),
                payload -> exposure(releaseFrom(payload))
        );

        SemanticMaterialisationException failure = assertThrows(
                SemanticMaterialisationException.class,
                () -> materialiser.materialise(
                        new DeploymentSemanticMaterialisationSet(
                                List.of(bundleFor("release-a"))
                        )
                )
        );

        assertEquals(
                SemanticMaterialisationFailure.RELEASE_IDENTITY_MISMATCH,
                failure.failure()
        );
    }

    @Test
    void duplicate_release_identity_is_distinguishable() {
        SemanticMaterialisationException failure = assertThrows(
                SemanticMaterialisationException.class,
                () -> new DeploymentSemanticMaterialisationSet(List.of(
                        bundleFor("release-a"),
                        bundleFor("release-a")
                ))
        );

        assertEquals(
                SemanticMaterialisationFailure.DUPLICATE_RELEASE_IDENTITY,
                failure.failure()
        );
    }

    @Test
    void definition_decode_failure_is_distinguishable() {
        SemanticReleaseMaterialiser materialiser = new SemanticReleaseMaterialiser(
                payload -> {
                    throw new IllegalArgumentException("cannot decode");
                },
                payload -> surface(releaseFrom(payload)),
                payload -> fulfilment(releaseFrom(payload)),
                payload -> exposure(releaseFrom(payload))
        );

        SemanticMaterialisationException failure = assertThrows(
                SemanticMaterialisationException.class,
                () -> materialiser.materialise(
                        new DeploymentSemanticMaterialisationSet(
                                List.of(bundleFor("release-a"))
                        )
                )
        );

        assertEquals(
                SemanticMaterialisationFailure.DEFINITION_DECODE_FAILURE,
                failure.failure()
        );
    }

    @Test
    void deployment_content_identity_is_order_independent_and_digest_sensitive() {
        DeploymentSemanticMaterialisationSet first =
                new DeploymentSemanticMaterialisationSet(List.of(
                        bundleFor("release-a"),
                        bundleFor("release-b")
                ));
        DeploymentSemanticMaterialisationSet reordered =
                new DeploymentSemanticMaterialisationSet(List.of(
                        bundleFor("release-b"),
                        bundleFor("release-a")
                ));
        DeploymentSemanticMaterialisationSet changed =
                new DeploymentSemanticMaterialisationSet(List.of(
                        bundleFor("release-a"),
                        bundle(
                                "release-b",
                                "semantic:release-b",
                                "surface:release-b",
                                "fulfilment:release-b",
                                "exposure:release-b-changed"
                        )
                ));

        assertEquals(first.contentIdentity(), reordered.contentIdentity());
        assertNotEquals(first.contentIdentity(), changed.contentIdentity());
    }

    private static SemanticReleaseMaterialiser materialiser() {
        return new SemanticReleaseMaterialiser(
                payload -> semantic(releaseFrom(payload)),
                payload -> surface(releaseFrom(payload)),
                payload -> fulfilment(releaseFrom(payload)),
                payload -> exposure(releaseFrom(payload))
        );
    }

    private static PackagedSemanticDefinitionBundle bundleFor(String release) {
        return bundle(
                release,
                "semantic:" + release,
                "surface:" + release,
                "fulfilment:" + release,
                "exposure:" + release
        );
    }

    private static PackagedSemanticDefinitionBundle bundle(
            String release,
            String semanticText,
            String surfaceText,
            String fulfilmentText,
            String exposureText
    ) {
        byte[] semantic = bytes(semanticText);
        byte[] surface = bytes(surfaceText);
        byte[] fulfilment = bytes(fulfilmentText);
        byte[] exposure = bytes(exposureText);
        String provenance = "published-release-evidence";
        String digest = PackagedSemanticDefinitionBundle.computeDigest(
                PackagedSemanticDefinitionBundle.CURRENT_FORMAT,
                release,
                provenance,
                semantic,
                surface,
                fulfilment,
                exposure
        );
        return new PackagedSemanticDefinitionBundle(
                PackagedSemanticDefinitionBundle.CURRENT_FORMAT,
                release,
                provenance,
                digest,
                semantic,
                surface,
                fulfilment,
                exposure
        );
    }

    private static PackagedSemanticDefinitionBundle legacyBundle(
            String format,
            String release,
            String semanticText,
            String surfaceText,
            String fulfilmentText
    ) {
        byte[] semantic = bytes(semanticText);
        byte[] surface = bytes(surfaceText);
        byte[] fulfilment = bytes(fulfilmentText);
        String provenance = "published-release-evidence";
        String digest = PackagedSemanticDefinitionBundle.computeDigest(
                format,
                release,
                provenance,
                semantic,
                surface,
                fulfilment
        );
        return new PackagedSemanticDefinitionBundle(
                format,
                release,
                provenance,
                digest,
                semantic,
                surface,
                fulfilment
        );
    }

    private static SemanticRegistrySnapshot semantic(String release) {
        return new SemanticRegistrySnapshot(release, Set.of());
    }

    private static SurfaceContributionRegistrySnapshot surface(String release) {
        return new SurfaceContributionRegistrySnapshot(release, Set.of());
    }

    private static FulfilmentContractRegistrySnapshot fulfilment(String release) {
        return new FulfilmentContractRegistrySnapshot(release, Set.of(), Set.of());
    }

    private static ExposureElementContractRegistrySnapshot exposure(String release) {
        return new ExposureElementContractRegistrySnapshot(release, Set.of());
    }

    private static String releaseFrom(byte[] payload) {
        String text = new String(payload, StandardCharsets.UTF_8);
        int separator = text.indexOf(':');
        if (separator < 0 || separator == text.length() - 1) {
            throw new IllegalArgumentException("invalid test payload");
        }
        return text.substring(separator + 1);
    }

    private static byte[] bytes(String value) {
        return value.getBytes(StandardCharsets.UTF_8);
    }
}
