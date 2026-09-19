package grandrue.commercial;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * MS-PROT-056 v1.10 — Initial Standard Commercial Catalogue Manifest.
 * Exact initial catalogue identities, grant cardinalities and conditional website support.
 */
class InitialStandardCommercialCatalogueTest {

    @Test
    void approved_initial_manifest_has_exact_identity_counts_and_monotonic_snapshots() {
        var manifest = InitialStandardCommercialCatalogue.manifest();
        var revision = manifest.revision();

        assertEquals("standard-commercial-catalogue@1", revision.catalogueRevisionIdentifier());
        assertEquals("standard-plan/free@1", revision.freePlan().revisionIdentifier());
        assertEquals("standard-plan/business@1", revision.businessPlan().revisionIdentifier());
        assertEquals("standard-plan/growth@1", revision.growthPlan().revisionIdentifier());

        assertEquals(36, manifest.bindings().size());
        assertEquals(13, revision.freePlan().entitlements().size());
        assertEquals(34, revision.businessPlan().entitlements().size());
        assertEquals(36, revision.growthPlan().entitlements().size());
        assertTrue(revision.businessPlan().entitlements().containsAll(revision.freePlan().entitlements()));
        assertTrue(revision.growthPlan().entitlements().containsAll(revision.businessPlan().entitlements()));
        assertNotEquals(revision.freePlan().entitlements(), revision.businessPlan().entitlements());
        assertNotEquals(revision.businessPlan().entitlements(), revision.growthPlan().entitlements());
    }

    @Test
    void every_approved_binding_has_one_unique_entitlement_and_exact_target_purpose_pair() {
        var manifest = InitialStandardCommercialCatalogue.manifest();
        assertEquals(36, manifest.bindings().stream()
                .map(CommercialAccessBinding::entitlementIdentity).distinct().count());
        assertEquals(36, manifest.bindings().stream()
                .map(binding -> binding.target() + "::" + binding.protectedPurpose()).distinct().count());

        assertTrue(manifest.bindings().stream().noneMatch(binding ->
                binding.entitlementIdentity().identifier().contains("*")
                        || binding.target().targetIdentity().contains("*")
                        || binding.protectedPurpose().contains("*")));
    }

    @Test
    void free_website_delivery_uses_platform_namespace_without_requiring_custom_domain() {
        var manifest = InitialStandardCommercialCatalogue.manifest();
        Map<String, CommercialAccessBinding> byEntitlement = manifest.bindings().stream()
                .collect(Collectors.toMap(
                        binding -> binding.entitlementIdentity().identifier(),
                        Function.identity()));

        var website = byEntitlement.get("commercial-entitlement/storefront/website-delivery@1");
        assertNotNull(website);
        assertTrue(website.supportingAccessRequirements().isEmpty());
        assertEquals(1, website.conditionalSupportingAccessRequirements().size());

        var conditional = website.conditionalSupportingAccessRequirements().iterator().next();
        assertEquals(2, conditional.alternatives().size());
        assertEquals(Set.of("PLATFORM_DELEGATED_NAMESPACE", "MERCHANT_CONTROLLED_DOMAIN"),
                conditional.alternatives().stream()
                        .map(CommercialConditionalSupportAlternative::conditionValue)
                        .collect(Collectors.toSet()));

        var free = manifest.revision().freePlan().entitlements();
        var business = manifest.revision().businessPlan().entitlements();
        var platform = new CommercialEntitlementIdentity(
                "commercial-entitlement/merchant-brand-infrastructure/platform-website-namespace-use@1");
        var custom = new CommercialEntitlementIdentity(
                "commercial-entitlement/merchant-brand-infrastructure/custom-website-domain-use@1");

        assertTrue(free.contains(platform));
        assertFalse(free.contains(custom));
        assertTrue(business.contains(platform));
        assertTrue(business.contains(custom));
    }

    @Test
    void reservations_and_pseudo_entitlements_are_absent() {
        var ids = InitialStandardCommercialCatalogue.manifest().bindings().stream()
                .map(binding -> binding.entitlementIdentity().identifier())
                .collect(Collectors.toSet());

        for (String prohibited : Set.of(
                "USE_AI", "USE_ASSISTANT", "AI_PREMIUM",
                "notification", "waitlist", "review", "reputation",
                "discoverability", "agentic-crm")) {
            assertTrue(ids.stream().noneMatch(id -> id.toLowerCase().contains(prohibited.toLowerCase())),
                    prohibited);
        }
    }
}
