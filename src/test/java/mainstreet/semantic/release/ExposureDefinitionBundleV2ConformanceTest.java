package mainstreet.semantic.release;

import mainstreet.fulfilment.FulfilmentContractRegistrySnapshot;
import mainstreet.semantic.registry.SemanticRegistrySnapshot;
import mainstreet.surface.ExposureElementContractRegistrySnapshot;
import mainstreet.surface.SurfaceContributionRegistrySnapshot;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExposureDefinitionBundleV2ConformanceTest {

    @Test
    void v1_meaning_is_preserved_and_current_complete_format_is_v2() {
        assertEquals(
                "mainstreet-semantic-bundle-v1",
                PackagedSemanticDefinitionBundle.V1_FORMAT
        );
        assertEquals(
                "mainstreet-semantic-bundle-v2",
                PackagedSemanticDefinitionBundle.CURRENT_FORMAT
        );
    }

    @Test
    void v2_integrity_covers_exposure_bytes_and_bundle_defensively_copies_them() {
        byte[] semantic = bytes("semantic:release-a");
        byte[] surface = bytes("surface:release-a");
        byte[] fulfilment = bytes("fulfilment:release-a");
        byte[] exposure = bytes("exposure:release-a:v1");
        byte[] changedExposure = bytes("exposure:release-a:v2");

        String digest = PackagedSemanticDefinitionBundle.computeDigest(
                PackagedSemanticDefinitionBundle.CURRENT_FORMAT,
                "release-a",
                "publication-a",
                semantic,
                surface,
                fulfilment,
                exposure
        );
        String changedDigest = PackagedSemanticDefinitionBundle.computeDigest(
                PackagedSemanticDefinitionBundle.CURRENT_FORMAT,
                "release-a",
                "publication-a",
                semantic,
                surface,
                fulfilment,
                changedExposure
        );

        assertNotEquals(digest, changedDigest);

        PackagedSemanticDefinitionBundle bundle = new PackagedSemanticDefinitionBundle(
                PackagedSemanticDefinitionBundle.CURRENT_FORMAT,
                "release-a",
                "publication-a",
                digest,
                semantic,
                surface,
                fulfilment,
                exposure
        );

        exposure[0] = 'X';
        assertArrayEquals(
                bytes("exposure:release-a:v1"),
                bundle.exposureDefinitions()
        );
        byte[] read = bundle.exposureDefinitions();
        read[0] = 'Y';
        assertArrayEquals(
                bytes("exposure:release-a:v1"),
                bundle.exposureDefinitions()
        );
    }

    @Test
    void materialisation_requires_exact_exposure_registry_and_exposes_it_from_assembly() {
        SemanticReleaseMaterialiser materialiser = new SemanticReleaseMaterialiser(
                payload -> semantic(releaseFrom(payload)),
                payload -> surface(releaseFrom(payload)),
                payload -> fulfilment(releaseFrom(payload)),
                payload -> exposure(releaseFrom(payload))
        );

        SemanticReleaseAssembly assembly = materialiser.materialise(
                new DeploymentSemanticMaterialisationSet(List.of(bundleFor("release-a")))
        ).release("release-a").orElseThrow();

        assertEquals(
                "release-a",
                assembly.exposureRegistry().semanticRegistryReleaseIdentifier()
        );
    }

    @Test
    void exposure_registry_release_mismatch_fails_closed() {
        SemanticReleaseMaterialiser materialiser = new SemanticReleaseMaterialiser(
                payload -> semantic(releaseFrom(payload)),
                payload -> surface(releaseFrom(payload)),
                payload -> fulfilment(releaseFrom(payload)),
                payload -> exposure("release-wrong")
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
    void legacy_v1_bundle_is_not_reinterpreted_as_complete_v2_materialisation() {
        byte[] semantic = bytes("semantic:release-a");
        byte[] surface = bytes("surface:release-a");
        byte[] fulfilment = bytes("fulfilment:release-a");
        String digest = PackagedSemanticDefinitionBundle.computeDigest(
                PackagedSemanticDefinitionBundle.V1_FORMAT,
                "release-a",
                "publication-a",
                semantic,
                surface,
                fulfilment
        );
        PackagedSemanticDefinitionBundle legacy = new PackagedSemanticDefinitionBundle(
                PackagedSemanticDefinitionBundle.V1_FORMAT,
                "release-a",
                "publication-a",
                digest,
                semantic,
                surface,
                fulfilment
        );

        SemanticReleaseMaterialiser materialiser = new SemanticReleaseMaterialiser(
                payload -> semantic(releaseFrom(payload)),
                payload -> surface(releaseFrom(payload)),
                payload -> fulfilment(releaseFrom(payload)),
                payload -> exposure(releaseFrom(payload))
        );

        SemanticMaterialisationException failure = assertThrows(
                SemanticMaterialisationException.class,
                () -> materialiser.materialise(
                        new DeploymentSemanticMaterialisationSet(List.of(legacy))
                )
        );

        assertEquals(
                SemanticMaterialisationFailure.UNSUPPORTED_BUNDLE_FORMAT,
                failure.failure()
        );
    }

    @Test
    void explicit_empty_exposure_registry_is_distinct_from_missing_exposure_evidence() {
        SemanticReleaseAssembly assembly = new SemanticReleaseAssembly(
                semantic("release-a"),
                surface("release-a"),
                fulfilment("release-a"),
                exposure("release-a")
        );

        assertEquals(Set.of(), assembly.exposureRegistry().contracts());
    }

    private static PackagedSemanticDefinitionBundle bundleFor(String release) {
        byte[] semantic = bytes("semantic:" + release);
        byte[] surface = bytes("surface:" + release);
        byte[] fulfilment = bytes("fulfilment:" + release);
        byte[] exposure = bytes("exposure:" + release);
        String digest = PackagedSemanticDefinitionBundle.computeDigest(
                PackagedSemanticDefinitionBundle.CURRENT_FORMAT,
                release,
                "publication-a",
                semantic,
                surface,
                fulfilment,
                exposure
        );
        return new PackagedSemanticDefinitionBundle(
                PackagedSemanticDefinitionBundle.CURRENT_FORMAT,
                release,
                "publication-a",
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
            throw new IllegalArgumentException("invalid test payload");
        }
        return text.substring(separator + 1);
    }

    private static byte[] bytes(String value) {
        return value.getBytes(StandardCharsets.UTF_8);
    }
}
