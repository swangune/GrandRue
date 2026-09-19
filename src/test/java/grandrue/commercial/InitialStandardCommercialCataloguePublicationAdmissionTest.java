package grandrue.commercial;

import grandrue.application.TrustedPlatformExecutionContext;
import grandrue.runtime.ExecutionPrincipal;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * MS-PROT-056 v1.9 §§7–8 and v1.10 §§26–31:
 * exact approved manifest content and explicit platform publication authority
 * are independent required predicates.
 */
class InitialStandardCommercialCataloguePublicationAdmissionTest {
    private static final TrustedPlatformExecutionContext PUBLISHER =
            new TrustedPlatformExecutionContext(new ExecutionPrincipal("catalogue-publisher"));

    @Test
    void exact_approved_manifest_is_admitted_only_after_current_platform_authorisation() {
        var checks = new AtomicInteger();
        CommercialCataloguePublicationAuthorisationAuthority authority = context -> {
            checks.incrementAndGet();
            return context.principal().identifier().equals("catalogue-publisher");
        };
        var admission = new InitialStandardCommercialCataloguePublicationAdmission(authority);

        assertDoesNotThrow(() -> admission.requirePublicationAuthority(PUBLISHER));
        assertDoesNotThrow(() -> admission.requireApprovedManifest(
                InitialStandardCommercialCatalogue.manifest()));
        assertEquals(1, checks.get());
    }

    @Test
    void trusted_attribution_without_explicit_publication_authorisation_is_rejected() {
        var admission = new InitialStandardCommercialCataloguePublicationAdmission(context -> false);

        var failure = assertThrows(
                CataloguePublicationException.class,
                () -> admission.requirePublicationAuthority(PUBLISHER));

        assertEquals(CataloguePublicationException.Reason.AUTHORISATION_REJECTED, failure.reason());
    }

    @Test
    void changed_manifest_content_is_not_accepted_by_reusing_approval_text() {
        var approved = InitialStandardCommercialCatalogue.manifest();
        var changed = new CommercialCatalogueManifest(
                approved.revision(),
                approved.bindings(),
                approved.allocationConformanceEvidence(),
                approved.approvalProvenanceReference() + "-forged");

        var admission = new InitialStandardCommercialCataloguePublicationAdmission(context -> true);
        var failure = assertThrows(
                CataloguePublicationException.class,
                () -> admission.requireApprovedManifest(changed));

        assertEquals(CataloguePublicationException.Reason.VALIDATION_REJECTED, failure.reason());
    }

    @Test
    void structurally_valid_but_unapproved_catalogue_is_rejected() {
        var approved = InitialStandardCommercialCatalogue.manifest();
        var revision = new StandardPlanCatalogueRevision(
                "unapproved-catalogue@1",
                new StandardPlanRevision(StandardPlanLevel.FREE, "unapproved-free@1", Set.of()),
                new StandardPlanRevision(StandardPlanLevel.BUSINESS, "unapproved-business@1", Set.of()),
                new StandardPlanRevision(StandardPlanLevel.GROWTH, "unapproved-growth@1", Set.of()));
        var unapproved = new CommercialCatalogueManifest(
                revision,
                Set.of(),
                approved.allocationConformanceEvidence(),
                approved.approvalProvenanceReference());

        var admission = new InitialStandardCommercialCataloguePublicationAdmission(context -> true);
        var failure = assertThrows(
                CataloguePublicationException.class,
                () -> admission.requireApprovedManifest(unapproved));

        assertEquals(CataloguePublicationException.Reason.VALIDATION_REJECTED, failure.reason());
    }

    @Test
    void publication_authority_is_current_and_not_cached_by_admission() {
        var calls = new AtomicInteger();
        var admission = new InitialStandardCommercialCataloguePublicationAdmission(context ->
                calls.incrementAndGet() == 1);

        assertDoesNotThrow(() -> admission.requirePublicationAuthority(PUBLISHER));
        var failure = assertThrows(
                CataloguePublicationException.class,
                () -> admission.requirePublicationAuthority(PUBLISHER));

        assertEquals(CataloguePublicationException.Reason.AUTHORISATION_REJECTED, failure.reason());
        assertEquals(2, calls.get());
    }
}
