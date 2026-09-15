package mainstreet.semantic.release;

import mainstreet.fulfilment.FulfilmentContractRegistrySnapshot;
import mainstreet.semantic.registry.SemanticRegistrySnapshot;
import mainstreet.surface.ExposureElementContractRegistrySnapshot;
import mainstreet.surface.SurfaceContributionRegistrySnapshot;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SemanticReleaseBootstrapTest {

    @Test
    void text_codec_round_trips_exact_published_bundle_evidence() {
        PackagedSemanticDefinitionBundle bundle = bundleFor("release-a");
        PackagedSemanticDefinitionBundleTextCodec codec =
                new PackagedSemanticDefinitionBundleTextCodec();

        PackagedSemanticDefinitionBundle decoded = codec.decode(codec.encode(bundle));

        assertEquals(bundle.formatVersion(), decoded.formatVersion());
        assertEquals(bundle.releaseIdentifier(), decoded.releaseIdentifier());
        assertEquals(bundle.publicationProvenance(), decoded.publicationProvenance());
        assertEquals(bundle.contentDigest(), decoded.contentDigest());
        assertArrayEquals(bundle.exposureDefinitions(), decoded.exposureDefinitions());
        assertTrue(decoded.integrityVerified());
    }

    @Test
    void malformed_resource_envelope_is_classified_as_unsupported_bundle_format() {
        PackagedSemanticDefinitionBundleTextCodec codec =
                new PackagedSemanticDefinitionBundleTextCodec();

        SemanticMaterialisationException failure = assertThrows(
                SemanticMaterialisationException.class,
                () -> codec.decode(bytes("not-a-valid-bundle-line"))
        );

        assertEquals(
                SemanticMaterialisationFailure.UNSUPPORTED_BUNDLE_FORMAT,
                failure.failure()
        );
    }

    @Test
    void classpath_loader_reads_only_explicit_assigned_resources() {
        PackagedSemanticDefinitionBundleTextCodec codec =
                new PackagedSemanticDefinitionBundleTextCodec();
        Map<String, byte[]> resources = Map.of(
                "semantic/release-a.msbundle", codec.encode(bundleFor("release-a")),
                "semantic/release-b.msbundle", codec.encode(bundleFor("release-b")),
                "semantic/unassigned.msbundle", codec.encode(bundleFor("release-c"))
        );
        ClasspathSemanticDefinitionBundleLoader loader =
                new ClasspathSemanticDefinitionBundleLoader(
                        new MapClassLoader(resources),
                        codec
                );

        DeploymentSemanticMaterialisationSet set = loader.load(List.of(
                "semantic/release-a.msbundle",
                "semantic/release-b.msbundle"
        ));

        assertEquals(2, set.bundles().size());
        assertEquals(
                Set.of("release-a", "release-b"),
                set.bundles().stream()
                        .map(PackagedSemanticDefinitionBundle::releaseIdentifier)
                        .collect(java.util.stream.Collectors.toUnmodifiableSet())
        );
    }

    @Test
    void missing_explicit_packaged_resource_fails_bootstrap_without_fallback() {
        ClasspathSemanticDefinitionBundleLoader loader =
                new ClasspathSemanticDefinitionBundleLoader(
                        new MapClassLoader(Map.of()),
                        new PackagedSemanticDefinitionBundleTextCodec()
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> loader.load(List.of("semantic/release-missing.msbundle"))
        );
    }

    @Test
    void bootstrap_requires_every_serving_release_to_be_materialised_exactly() {
        PackagedSemanticDefinitionBundleTextCodec codec =
                new PackagedSemanticDefinitionBundleTextCodec();
        ClasspathSemanticDefinitionBundleLoader loader =
                new ClasspathSemanticDefinitionBundleLoader(
                        new MapClassLoader(Map.of(
                                "semantic/release-a.msbundle",
                                codec.encode(bundleFor("release-a"))
                        )),
                        codec
                );
        SemanticReleaseBootstrap bootstrap = new SemanticReleaseBootstrap(
                loader,
                materialiser()
        );

        SemanticMaterialisationException failure = assertThrows(
                SemanticMaterialisationException.class,
                () -> bootstrap.bootstrap(
                        List.of("semantic/release-a.msbundle"),
                        Set.of("release-a", "release-b")
                )
        );
        assertEquals(
                SemanticMaterialisationFailure.REQUIRED_RELEASE_NOT_MATERIALISED,
                failure.failure()
        );
    }

    @Test
    void bootstrap_publishes_exact_repository_only_after_complete_coverage() {
        PackagedSemanticDefinitionBundleTextCodec codec =
                new PackagedSemanticDefinitionBundleTextCodec();
        ClasspathSemanticDefinitionBundleLoader loader =
                new ClasspathSemanticDefinitionBundleLoader(
                        new MapClassLoader(Map.of(
                                "semantic/release-a.msbundle",
                                codec.encode(bundleFor("release-a")),
                                "semantic/release-b.msbundle",
                                codec.encode(bundleFor("release-b"))
                        )),
                        codec
                );
        SemanticReleaseBootstrap bootstrap = new SemanticReleaseBootstrap(
                loader,
                materialiser()
        );

        SemanticReleaseAssemblyRepository repository = bootstrap.bootstrap(
                List.of(
                        "semantic/release-a.msbundle",
                        "semantic/release-b.msbundle"
                ),
                Set.of("release-a", "release-b")
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

    private static SemanticReleaseMaterialiser materialiser() {
        return new SemanticReleaseMaterialiser(
                payload -> semantic(releaseFrom(payload)),
                payload -> surface(releaseFrom(payload)),
                payload -> fulfilment(releaseFrom(payload)),
                payload -> exposure(releaseFrom(payload))
        );
    }

    private static PackagedSemanticDefinitionBundle bundleFor(String release) {
        byte[] semantic = bytes("semantic:" + release);
        byte[] surface = bytes("surface:" + release);
        byte[] fulfilment = bytes("fulfilment:" + release);
        byte[] exposure = bytes("exposure:" + release);
        String provenance = "published-by-platform-release-pipeline";
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

    private static final class MapClassLoader extends ClassLoader {

        private final Map<String, byte[]> resources;

        private MapClassLoader(Map<String, byte[]> resources) {
            super(null);
            this.resources = Map.copyOf(resources);
        }

        @Override
        public InputStream getResourceAsStream(String name) {
            byte[] content = resources.get(name);
            return content == null ? null : new ByteArrayInputStream(content);
        }
    }
}
