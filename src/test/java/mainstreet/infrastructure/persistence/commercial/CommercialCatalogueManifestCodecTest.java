package mainstreet.infrastructure.persistence.commercial;

import mainstreet.commercial.*;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/** MS-PROT-056 v1.9, designs/MS-PROT-056 v1.9 — Commercial Catalogue Binding, Publication & Historical Resolution Amendment.md, §13 — Retention, recovery and runtime boundaries. */
class CommercialCatalogueManifestCodecTest {
    private final CommercialCatalogueManifestCodec codec = new CommercialCatalogueManifestCodec();

    @Test
    void all_retained_fields_round_trip_without_a_live_definition_registry() {
        var manifest = fixture(false);
        assertEquals(manifest, codec.decode(codec.encode(manifest)));
    }

    @Test
    void equivalent_set_order_produces_identical_retained_bytes() {
        assertArrayEquals(codec.encode(fixture(false)), codec.encode(fixture(true)));
    }

    @Test
    void truncated_lengths_and_trailing_content_fail_integrity_checks() {
        var encoded = codec.encode(fixture(false));
        for (int size : new int[]{0, 2, 4, encoded.length / 2, encoded.length - 1}) {
            assertIntegrity(Arrays.copyOf(encoded, size));
        }
        assertIntegrity(Arrays.copyOf(encoded, encoded.length + 1));
        var impossibleLength = encoded.clone();
        ByteBuffer.wrap(impossibleLength).putInt(4, Integer.MAX_VALUE);
        assertIntegrity(impossibleLength);
    }

    @Test
    void unsupported_retained_encoding_is_not_silently_reinterpreted() {
        var encoded = codec.encode(fixture(false));
        ByteBuffer.wrap(encoded).putInt(0, 2);
        assertEquals(CatalogueResolutionException.Reason.TECHNICAL_FAILURE,
                assertThrows(CatalogueResolutionException.class, () -> codec.decode(encoded)).reason());
    }

    @Test
    void unicode_and_long_evidence_references_are_not_truncated() {
        var original = fixture(false);
        var manifest = new CommercialCatalogueManifest(original.revision(), original.bindings(),
                Set.of("fixture-évidence-".repeat(6000)), "fixture-approbation-東京");
        assertEquals(manifest, codec.decode(codec.encode(manifest)));
    }

    private void assertIntegrity(byte[] content) {
        assertEquals(CatalogueResolutionException.Reason.INTEGRITY_FAILURE,
                assertThrows(CatalogueResolutionException.class, () -> codec.decode(content)).reason());
    }

    private static CommercialCatalogueManifest fixture(boolean reverse) {
        var root = new CommercialEntitlementIdentity("fixture-read");
        var view = new CommercialEntitlementIdentity("fixture-view");
        var target = new CommercialAccessTarget("fixture-owner", "fixture/read", "3");
        var support = new CommercialAccessTarget("fixture-presenter", "fixture/present", "2");
        var requirement = new CommercialSupportingAccessRequirement(support, Set.of("PRESENT"), "fixture-support-authority");
        var readBinding = new CommercialAccessBinding(root, CommercialEntitlementTargetKind.OPERATION_ACCESS,
                target, "READ", "fixture-read-authority", Set.of(requirement), "fixture-read-boundary");
        var viewBinding = new CommercialAccessBinding(view, CommercialEntitlementTargetKind.PRESENTATION_PRIVILEGE,
                support, "PRESENT", "fixture-view-authority", Set.of(), "fixture-view-boundary");
        var grants = new LinkedHashSet<>(reverse ? List.of(view, root) : List.of(root, view));
        var bindings = new LinkedHashSet<>(reverse ? List.of(viewBinding, readBinding) : List.of(readBinding, viewBinding));
        var evidence = new LinkedHashSet<>(reverse ? List.of("fixture-B", "fixture-A") : List.of("fixture-A", "fixture-B"));
        var revision = new StandardPlanCatalogueRevision("fixture-generation",
                new StandardPlanRevision(StandardPlanLevel.FREE, "fixture-free", Set.of()),
                new StandardPlanRevision(StandardPlanLevel.BUSINESS, "fixture-business", grants),
                new StandardPlanRevision(StandardPlanLevel.GROWTH, "fixture-growth", grants));
        return new CommercialCatalogueManifest(revision, bindings, evidence, "fixture-approval");
    }
}
