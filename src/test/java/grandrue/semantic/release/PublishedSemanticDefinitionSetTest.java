package grandrue.semantic.release;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PublishedSemanticDefinitionSetTest {

    @Test
    void published_evidence_is_immutable_and_release_affined() {
        byte[] semantic = bytes("semantic:release-a");
        byte[] surface = bytes("surface:release-a");
        byte[] fulfilment = bytes("fulfilment:release-a");
        byte[] exposure = bytes("exposure:release-a");

        PublishedSemanticDefinitionSet evidence = new PublishedSemanticDefinitionSet(
                "release-a",
                "publication-evidence-001",
                semantic,
                surface,
                fulfilment,
                exposure
        );

        semantic[0] = 'X';
        surface[0] = 'X';
        fulfilment[0] = 'X';
        exposure[0] = 'X';

        assertEquals("release-a", evidence.releaseIdentifier());
        assertEquals("publication-evidence-001", evidence.publicationProvenance());
        assertArrayEquals(bytes("semantic:release-a"), evidence.semanticDefinitions());
        assertArrayEquals(bytes("surface:release-a"), evidence.surfaceDefinitions());
        assertArrayEquals(bytes("fulfilment:release-a"), evidence.fulfilmentDefinitions());
        assertArrayEquals(bytes("exposure:release-a"), evidence.exposureDefinitions());
        assertNotSame(evidence.semanticDefinitions(), evidence.semanticDefinitions());
        assertNotSame(evidence.exposureDefinitions(), evidence.exposureDefinitions());
    }

    @Test
    void packager_preserves_published_evidence_without_relabelling() {
        PublishedSemanticDefinitionSet evidence = evidence(
                "release-a",
                "publication-evidence-001",
                "semantic:release-a",
                "exposure:release-a"
        );

        PackagedSemanticDefinitionBundle bundle =
                new SemanticDefinitionBundlePackager().packageEvidence(evidence);

        assertEquals(PackagedSemanticDefinitionBundle.CURRENT_FORMAT, bundle.formatVersion());
        assertEquals(evidence.releaseIdentifier(), bundle.releaseIdentifier());
        assertEquals(evidence.publicationProvenance(), bundle.publicationProvenance());
        assertArrayEquals(evidence.semanticDefinitions(), bundle.semanticDefinitions());
        assertArrayEquals(evidence.surfaceDefinitions(), bundle.surfaceDefinitions());
        assertArrayEquals(evidence.fulfilmentDefinitions(), bundle.fulfilmentDefinitions());
        assertArrayEquals(evidence.exposureDefinitions(), bundle.exposureDefinitions());
        assertTrue(bundle.integrityVerified());
    }

    @Test
    void changed_published_exposure_evidence_changes_packaged_content_identity() {
        SemanticDefinitionBundlePackager packager = new SemanticDefinitionBundlePackager();

        PackagedSemanticDefinitionBundle first = packager.packageEvidence(evidence(
                "release-a",
                "publication-evidence-001",
                "semantic:release-a",
                "exposure:release-a"
        ));
        PackagedSemanticDefinitionBundle changed = packager.packageEvidence(evidence(
                "release-a",
                "publication-evidence-001",
                "semantic:release-a",
                "exposure:release-a-changed"
        ));

        assertNotEquals(first.contentDigest(), changed.contentDigest());
    }

    @Test
    void legacy_published_evidence_is_not_silently_packaged_as_v2() {
        PublishedSemanticDefinitionSet legacy = new PublishedSemanticDefinitionSet(
                "release-a",
                "publication-evidence-001",
                bytes("semantic:release-a"),
                bytes("surface:release-a"),
                bytes("fulfilment:release-a")
        );

        assertTrue(!legacy.hasExposureDefinitions());
        assertThrows(IllegalStateException.class, legacy::exposureDefinitions);
        assertThrows(
                IllegalArgumentException.class,
                () -> new SemanticDefinitionBundlePackager().packageEvidence(legacy)
        );
    }

    private static PublishedSemanticDefinitionSet evidence(
            String release,
            String provenance,
            String semanticText,
            String exposureText
    ) {
        return new PublishedSemanticDefinitionSet(
                release,
                provenance,
                bytes(semanticText),
                bytes("surface:" + release),
                bytes("fulfilment:" + release),
                bytes(exposureText)
        );
    }

    private static byte[] bytes(String value) {
        return value.getBytes(StandardCharsets.UTF_8);
    }
}
