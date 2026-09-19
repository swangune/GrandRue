package grandrue.semantic.release;

import grandrue.fulfilment.FulfilmentContractRegistrySnapshot;
import grandrue.semantic.registry.SemanticRegistrySnapshot;
import grandrue.surface.ExposureElementContractRegistrySnapshot;
import grandrue.surface.SurfaceContributionRegistrySnapshot;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SemanticDefinitionBundleMaterialisationTest {

    private static final String FORMAT = PackagedSemanticDefinitionBundle.CURRENT_FORMAT;

    @Test
    void immutable_bundle_validates_integrity_and_defensively_copies_sections() {
        byte[] semantic = bytes("semantic:release-a");
        byte[] surface = bytes("surface:release-a");
        byte[] fulfilment = bytes("fulfilment:release-a");
        byte[] exposure = bytes("exposure:release-a");

        PackagedSemanticDefinitionBundle bundle = bundle(
                "release-a",
                semantic,
                surface,
                fulfilment,
                exposure
        );

        semantic[0] = 'X';
        surface[0] = 'X';
        fulfilment[0] = 'X';
        exposure[0] = 'X';

        assertArrayEquals(bytes("semantic:release-a"), bundle.semanticDefinitions());
        assertArrayEquals(bytes("surface:release-a"), bundle.surfaceDefinitions());
        assertArrayEquals(bytes("fulfilment:release-a"), bundle.fulfilmentDefinitions());
        assertArrayEquals(bytes("exposure:release-a"), bundle.exposureDefinitions());

        byte[] read = bundle.semanticDefinitions();
        assertNotSame(read, bundle.semanticDefinitions());
        read[0] = 'Y';
        assertArrayEquals(bytes("semantic:release-a"), bundle.semanticDefinitions());
        assertTrue(bundle.integrityVerified());
    }

    @Test
    void corrupted_published_content_is_rejected_before_decoding() {
        PackagedSemanticDefinitionBundle valid = bundleFor("release-a");

        SemanticMaterialisationException failure = assertThrows(
                SemanticMaterialisationException.class,
                () -> new PackagedSemanticDefinitionBundle(
                        FORMAT,
                        valid.releaseIdentifier(),
                        valid.publicationProvenance(),
                        valid.contentDigest(),
                        bytes("semantic:CORRUPTED"),
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
    void materialises_multiple_exact_releases_without_latest_substitution() {
        SemanticReleaseMaterialiser materialiser = materialiser();

        SemanticReleaseAssemblyRepository repository = materialiser.materialise(
                new DeploymentSemanticMaterialisationSet(List.of(
                        bundleFor("release-a"),
                        bundleFor("release-b")
                ))
        );

        assertEquals(
                "release-a",
                repository.release("release-a").orElseThrow().releaseIdentifier()
        );
        assertEquals(
                "release-b",
                repository.release("release-b").orElseThrow().releaseIdentifier()
        );
        assertEquals(
                "release-a",
                repository.release("release-a").orElseThrow()
                        .exposureRegistry().semanticRegistryReleaseIdentifier()
        );
        assertTrue(repository.release("release-c").isEmpty());
    }

    @Test
    void decoded_constituent_with_wrong_release_affinity_is_rejected() {
        SemanticReleaseMaterialiser materialiser = new SemanticReleaseMaterialiser(
                payload -> semantic("release-wrong"),
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
    void deployment_set_rejects_duplicate_exact_release_identity() {
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
    void invalid_bundle_prevents_repository_publication_as_a_whole() {
        SemanticReleaseMaterialiser materialiser = new SemanticReleaseMaterialiser(
                payload -> {
                    String release = releaseFrom(payload);
                    if ("release-b".equals(release)) {
                        throw new IllegalArgumentException("invalid semantic payload");
                    }
                    return semantic(release);
                },
                payload -> surface(releaseFrom(payload)),
                payload -> fulfilment(releaseFrom(payload)),
                payload -> exposure(releaseFrom(payload))
        );

        SemanticMaterialisationException failure = assertThrows(
                SemanticMaterialisationException.class,
                () -> materialiser.materialise(
                        new DeploymentSemanticMaterialisationSet(List.of(
                                bundleFor("release-a"),
                                bundleFor("release-b")
                        ))
                )
        );
        assertEquals(
                SemanticMaterialisationFailure.DEFINITION_DECODE_FAILURE,
                failure.failure()
        );
    }

    @Test
    void provenance_and_release_identity_are_mandatory_published_evidence() {
        byte[] semantic = bytes("semantic:release-a");
        byte[] surface = bytes("surface:release-a");
        byte[] fulfilment = bytes("fulfilment:release-a");
        byte[] exposure = bytes("exposure:release-a");
        String digest = PackagedSemanticDefinitionBundle.computeDigest(
                FORMAT,
                "release-a",
                "publication-2026-08-28",
                semantic,
                surface,
                fulfilment,
                exposure
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new PackagedSemanticDefinitionBundle(
                        FORMAT,
                        " ",
                        "publication-2026-08-28",
                        digest,
                        semantic,
                        surface,
                        fulfilment,
                        exposure
                )
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new PackagedSemanticDefinitionBundle(
                        FORMAT,
                        "release-a",
                        " ",
                        digest,
                        semantic,
                        surface,
                        fulfilment,
                        exposure
                )
        );
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
                bytes("semantic:" + release),
                bytes("surface:" + release),
                bytes("fulfilment:" + release),
                bytes("exposure:" + release)
        );
    }

    private static PackagedSemanticDefinitionBundle bundle(
            String release,
            byte[] semantic,
            byte[] surface,
            byte[] fulfilment,
            byte[] exposure
    ) {
        String provenance = "publication-2026-08-28";
        String digest = PackagedSemanticDefinitionBundle.computeDigest(
                FORMAT,
                release,
                provenance,
                semantic,
                surface,
                fulfilment,
                exposure
        );
        return new PackagedSemanticDefinitionBundle(
                FORMAT,
                release,
                provenance,
                digest,
                semantic,
                surface,
                fulfilment,
                exposure
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
            throw new IllegalArgumentException("invalid test semantic payload");
        }
        return text.substring(separator + 1);
    }

    private static byte[] bytes(String value) {
        return value.getBytes(StandardCharsets.UTF_8);
    }
}
