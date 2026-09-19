package grandrue.commercial;

import grandrue.application.TrustedPlatformExecutionContext;
import grandrue.runtime.ExecutionPrincipal;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * MS-PROT-056 v1.9 §§8–12 and v1.10 §§26–31:
 * initial publication remains an explicit trusted Commercial operation and
 * ordinary new-account rollout fails closed until the approved generation
 * actually exists.
 */
class InitialStandardCommercialCatalogueRolloutTest {
    private static final TrustedPlatformExecutionContext PUBLISHER =
            new TrustedPlatformExecutionContext(new ExecutionPrincipal("catalogue-publisher"));

    @Test
    void initial_publication_uses_exact_approved_manifest_and_no_predecessor() {
        var store = new RecordingStore();
        var rollout = new InitialStandardCommercialCatalogueRollout(store);

        rollout.publishInitial("publish-initial-1", PUBLISHER);

        assertEquals("publish-initial-1", store.lastRequest.requestIdentifier());
        assertEquals(InitialStandardCommercialCatalogue.manifest(), store.lastRequest.manifest());
        assertEquals(Optional.empty(), store.lastRequest.expectedPredecessor());
        assertSame(PUBLISHER, store.lastContext);
    }

    @Test
    void ordinary_account_path_is_not_ready_before_initial_publication() {
        var rollout = new InitialStandardCommercialCatalogueRollout(new RecordingStore());

        var failure = assertThrows(
                CatalogueResolutionException.class,
                rollout::requireOrdinaryMerchantAccountPathReady);

        assertEquals(CatalogueResolutionException.Reason.NOT_ESTABLISHED, failure.reason());
    }

    @Test
    void exact_approved_published_generation_makes_rollout_ready() {
        var store = new RecordingStore();
        var rollout = new InitialStandardCommercialCatalogueRollout(store);
        rollout.publishInitial("publish-initial-1", PUBLISHER);

        assertDoesNotThrow(rollout::requireOrdinaryMerchantAccountPathReady);
    }

    @Test
    void same_catalogue_identity_with_nonapproved_content_fails_integrity() {
        var approved = InitialStandardCommercialCatalogue.manifest();
        var changed = new CommercialCatalogueManifest(
                approved.revision(),
                approved.bindings(),
                approved.allocationConformanceEvidence(),
                approved.approvalProvenanceReference() + "-changed");
        var store = new RecordingStore();
        store.publication = publication(changed);

        var failure = assertThrows(
                CatalogueResolutionException.class,
                new InitialStandardCommercialCatalogueRollout(store)
                        ::requireOrdinaryMerchantAccountPathReady);

        assertEquals(CatalogueResolutionException.Reason.INTEGRITY_FAILURE, failure.reason());
    }

    private static CommercialCataloguePublication publication(CommercialCatalogueManifest manifest) {
        return new CommercialCataloguePublication(
                "publish-initial-1",
                manifest,
                Optional.empty(),
                "catalogue-publisher",
                Instant.parse("2026-09-19T19:00:00Z"));
    }

    private static final class RecordingStore implements CommercialCatalogueStore {
        private CommercialCataloguePublicationRequest lastRequest;
        private TrustedPlatformExecutionContext lastContext;
        private CommercialCataloguePublication publication;

        @Override
        public CommercialCataloguePublication publish(
                CommercialCataloguePublicationRequest request,
                TrustedPlatformExecutionContext context) {
            lastRequest = request;
            lastContext = context;
            publication = publication(request.manifest());
            return publication;
        }

        @Override
        public Optional<CommercialCataloguePublication> exactGeneration(String catalogueIdentifier) {
            return Optional.ofNullable(publication)
                    .filter(value -> value.manifest().revision()
                            .catalogueRevisionIdentifier().equals(catalogueIdentifier));
        }

        @Override
        public CommercialCataloguePublication effectiveAt(Instant instant) {
            if (publication == null) {
                throw new CatalogueResolutionException(
                        CatalogueResolutionException.Reason.NOT_ESTABLISHED,
                        "fixture catalogue absent");
            }
            return publication;
        }
    }
}
