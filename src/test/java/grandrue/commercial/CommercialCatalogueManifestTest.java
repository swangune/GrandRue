package grandrue.commercial;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/** MS-PROT-056 v1.9, designs/MS-PROT-056 v1.9 — Commercial Catalogue Binding, Publication & Historical Resolution Amendment.md, §§4–6, 8, 13. */
class CommercialCatalogueManifestTest {
    private static final CommercialAccessTarget TARGET = target("fixture-owner", "fixture/read", "1");
    private static final CommercialAccessTarget SUPPORT = target("fixture-view", "fixture/present", "1");
    private static final CommercialEntitlementIdentity READ = entitlement("fixture-read");
    private static final CommercialEntitlementIdentity VIEW = entitlement("fixture-view");

    @Test
    void retains_explicit_plans_bindings_and_evidence_without_publishing_or_approving_them() {
        var binding = binding(READ, TARGET, "READ", Set.of());
        var manifest = manifest("one", Set.of(READ), Set.of(READ), Set.of(binding));
        assertEquals(Set.of(binding), manifest.bindings());
        assertEquals(Set.of("fixture-allocation-evidence"), manifest.allocationConformanceEvidence());
        assertEquals("fixture-approval-reference", manifest.approvalProvenanceReference());
        assertEquals(Set.of(READ), manifest.revision().freePlan().entitlements());
    }

    @Test
    void every_grant_requires_a_retained_binding() {
        assertThrows(IllegalArgumentException.class,
                () -> manifest("one", Set.of(READ), Set.of(READ), Set.of()));
    }

    @Test
    void one_entitlement_cannot_have_two_binding_meanings() {
        assertThrows(IllegalArgumentException.class, () -> manifest("one", Set.of(), Set.of(READ), Set.of(
                binding(READ, TARGET, "READ", Set.of()), binding(READ, TARGET, "WRITE", Set.of()))));
    }

    @Test
    void exact_target_and_purpose_cannot_have_two_entitlement_identities() {
        assertThrows(IllegalArgumentException.class, () -> manifest("one", Set.of(), Set.of(READ, VIEW), Set.of(
                binding(READ, TARGET, "READ", Set.of()), binding(VIEW, TARGET, "READ", Set.of()))));
    }

    @Test
    void owner_and_contract_revision_are_part_of_the_exact_target() {
        for (var different : Set.of(target("another-owner", "fixture/read", "1"),
                target("fixture-owner", "fixture/read", "2"))) {
            assertDoesNotThrow(() -> manifest("one", Set.of(), Set.of(READ, VIEW), Set.of(
                    binding(READ, TARGET, "READ", Set.of()), binding(VIEW, different, "READ", Set.of()))));
        }
    }

    @Test
    void different_purposes_at_one_target_may_have_different_entitlements() {
        assertDoesNotThrow(() -> manifest("one", Set.of(), Set.of(READ, VIEW), Set.of(
                binding(READ, TARGET, "READ", Set.of()), binding(VIEW, TARGET, "WRITE", Set.of()))));
    }

    @Test
    void required_support_purpose_must_resolve_to_an_exact_binding() {
        var required = requirement(SUPPORT, Set.of("PRESENT"));
        assertThrows(IllegalArgumentException.class, () -> manifest("one", Set.of(), Set.of(READ),
                Set.of(binding(READ, TARGET, "READ", Set.of(required)))));
    }

    @Test
    void support_cannot_require_a_higher_tier_than_the_granted_service() {
        var bindings = supportedBindings();
        assertThrows(IllegalArgumentException.class,
                () -> manifest("one", Set.of(READ), Set.of(READ, VIEW), bindings));
        assertDoesNotThrow(() -> manifest("one", Set.of(READ, VIEW), Set.of(READ, VIEW), bindings));
    }

    @Test
    void every_required_support_purpose_must_be_satisfied() {
        var root = binding(READ, TARGET, "READ", Set.of(requirement(SUPPORT, Set.of("PRESENT", "EXPORT"))));
        var present = binding(VIEW, SUPPORT, "PRESENT", Set.of());
        assertThrows(IllegalArgumentException.class,
                () -> manifest("one", Set.of(), Set.of(READ, VIEW), Set.of(root, present)));
    }

    @Test
    void support_closure_includes_transitive_dependencies() {
        var export = entitlement("fixture-export");
        var exportTarget = target("fixture-exporter", "fixture/export", "1");
        var root = binding(READ, TARGET, "READ", Set.of(requirement(SUPPORT, Set.of("PRESENT"))));
        var present = binding(VIEW, SUPPORT, "PRESENT", Set.of(requirement(exportTarget, Set.of("EXPORT"))));
        var exporting = binding(export, exportTarget, "EXPORT", Set.of());
        var bindings = Set.of(root, present, exporting);
        assertThrows(IllegalArgumentException.class,
                () -> manifest("one", Set.of(READ, VIEW), Set.of(READ, VIEW, export), bindings));
        assertDoesNotThrow(() -> manifest("one", Set.of(READ, VIEW, export), Set.of(READ, VIEW, export), bindings));
    }

    @Test
    void business_support_must_not_be_available_only_in_growth() {
        var revisions = new StandardPlanCatalogueRevision("one",
                new StandardPlanRevision(StandardPlanLevel.FREE, "one-free", Set.of()),
                new StandardPlanRevision(StandardPlanLevel.BUSINESS, "one-business", Set.of(READ)),
                new StandardPlanRevision(StandardPlanLevel.GROWTH, "one-growth", Set.of(READ, VIEW)));
        assertThrows(IllegalArgumentException.class, () -> new CommercialCatalogueManifest(
                revisions, supportedBindings(), Set.of("fixture-evidence"), "fixture-approval"));
    }

    @Test
    void explicit_no_independent_entitlement_support_retains_its_classification_evidence() {
        var support = requirement(SUPPORT, Set.of());
        var root = binding(READ, TARGET, "READ", Set.of(support));
        assertDoesNotThrow(() -> manifest("one", Set.of(READ), Set.of(READ), Set.of(root)));
        assertEquals("fixture-classification-authority", support.classificationAuthority());
    }

    @Test
    void conflicting_support_classifications_are_rejected() {
        var root = binding(READ, TARGET, "READ", Set.of(requirement(SUPPORT, Set.of())));
        var protectedSupport = binding(VIEW, SUPPORT, "PRESENT", Set.of());
        assertThrows(IllegalArgumentException.class,
                () -> manifest("one", Set.of(), Set.of(READ, VIEW), Set.of(root, protectedSupport)));
    }

    @Test
    void conditional_support_requires_one_available_alternative_not_every_alternative() {
        var platform = entitlement("fixture-platform");
        var custom = entitlement("fixture-custom");
        var platformTarget = target("fixture-brand", "fixture/platform-namespace", "1");
        var customTarget = target("fixture-brand", "fixture/custom-domain", "1");
        var alternatives = Set.of(
                new CommercialConditionalSupportAlternative(
                        "PLATFORM_DELEGATED_NAMESPACE", platformTarget, Set.of("USE_PLATFORM")),
                new CommercialConditionalSupportAlternative(
                        "MERCHANT_CONTROLLED_DOMAIN", customTarget, Set.of("USE_CUSTOM")));
        var conditional = new CommercialConditionalSupportingAccessRequirement(
                "fixture-namespace-family-authority", alternatives, "fixture-classification-authority");
        var root = new CommercialAccessBinding(
                READ, CommercialEntitlementTargetKind.PLATFORM_SERVICE_ACCESS, TARGET, "SERVE",
                "fixture-target-authority", Set.of(), Set.of(conditional), "fixture-boundary");
        var platformBinding = binding(platform, platformTarget, "USE_PLATFORM", Set.of());
        var customBinding = binding(custom, customTarget, "USE_CUSTOM", Set.of());

        var revisions = new StandardPlanCatalogueRevision("one",
                new StandardPlanRevision(StandardPlanLevel.FREE, "one-free", Set.of(READ, platform)),
                new StandardPlanRevision(StandardPlanLevel.BUSINESS, "one-business", Set.of(READ, platform, custom)),
                new StandardPlanRevision(StandardPlanLevel.GROWTH, "one-growth", Set.of(READ, platform, custom)));

        assertDoesNotThrow(() -> new CommercialCatalogueManifest(
                revisions, Set.of(root, platformBinding, customBinding),
                Set.of("fixture-evidence"), "fixture-approval"));
    }

    @Test
    void conditional_support_fails_when_a_granted_plan_has_no_satisfied_alternative() {
        var platform = entitlement("fixture-platform");
        var custom = entitlement("fixture-custom");
        var platformTarget = target("fixture-brand", "fixture/platform-namespace", "1");
        var customTarget = target("fixture-brand", "fixture/custom-domain", "1");
        var conditional = new CommercialConditionalSupportingAccessRequirement(
                "fixture-namespace-family-authority",
                Set.of(
                        new CommercialConditionalSupportAlternative(
                                "PLATFORM_DELEGATED_NAMESPACE", platformTarget, Set.of("USE_PLATFORM")),
                        new CommercialConditionalSupportAlternative(
                                "MERCHANT_CONTROLLED_DOMAIN", customTarget, Set.of("USE_CUSTOM"))),
                "fixture-classification-authority");
        var root = new CommercialAccessBinding(
                READ, CommercialEntitlementTargetKind.PLATFORM_SERVICE_ACCESS, TARGET, "SERVE",
                "fixture-target-authority", Set.of(), Set.of(conditional), "fixture-boundary");
        var platformBinding = binding(platform, platformTarget, "USE_PLATFORM", Set.of());
        var customBinding = binding(custom, customTarget, "USE_CUSTOM", Set.of());

        var revisions = new StandardPlanCatalogueRevision("one",
                new StandardPlanRevision(StandardPlanLevel.FREE, "one-free", Set.of(READ)),
                new StandardPlanRevision(StandardPlanLevel.BUSINESS, "one-business", Set.of(READ, platform, custom)),
                new StandardPlanRevision(StandardPlanLevel.GROWTH, "one-growth", Set.of(READ, platform, custom)));

        assertThrows(IllegalArgumentException.class, () -> new CommercialCatalogueManifest(
                revisions, Set.of(root, platformBinding, customBinding),
                Set.of("fixture-evidence"), "fixture-approval"));
    }

    @Test
    void every_conditional_alternative_must_resolve_to_an_exact_binding() {
        var missingTarget = target("fixture-brand", "fixture/missing-namespace", "1");
        var conditional = new CommercialConditionalSupportingAccessRequirement(
                "fixture-namespace-family-authority",
                Set.of(new CommercialConditionalSupportAlternative(
                        "MISSING", missingTarget, Set.of("USE_MISSING"))),
                "fixture-classification-authority");
        var root = new CommercialAccessBinding(
                READ, CommercialEntitlementTargetKind.PLATFORM_SERVICE_ACCESS, TARGET, "SERVE",
                "fixture-target-authority", Set.of(), Set.of(conditional), "fixture-boundary");

        assertThrows(IllegalArgumentException.class, () -> manifest(
                "one", Set.of(READ), Set.of(READ), Set.of(root)));
    }

    @Test
    void immutable_snapshots_do_not_retain_mutable_caller_collections() {
        var purposes = new HashSet<>(Set.of("PRESENT"));
        var support = requirement(SUPPORT, purposes);
        purposes.clear();
        assertEquals(Set.of("PRESENT"), support.requiredPurposes());
        var supports = new HashSet<>(Set.of(support));
        var root = binding(READ, TARGET, "READ", supports);
        supports.clear();
        var bindings = new HashSet<>(Set.of(root, binding(VIEW, SUPPORT, "PRESENT", Set.of())));
        var evidence = new HashSet<>(Set.of("fixture-evidence"));
        var manifest = new CommercialCatalogueManifest(revision("one", Set.of(READ, VIEW), Set.of(READ, VIEW)),
                bindings, evidence, "fixture-approval");
        bindings.clear();
        evidence.clear();
        assertEquals(2, manifest.bindings().size());
        assertEquals(Set.of("fixture-evidence"), manifest.allocationConformanceEvidence());
        assertThrows(UnsupportedOperationException.class, () -> manifest.bindings().clear());
        assertThrows(UnsupportedOperationException.class, () -> root.supportingAccessRequirements().clear());
    }

    @Test
    void mandatory_metadata_and_explicit_classification_cannot_be_missing() {
        assertThrows(IllegalArgumentException.class, () -> target(" ", "fixture/read", "1"));
        assertThrows(IllegalArgumentException.class, () -> target("owner", "", "1"));
        assertThrows(IllegalArgumentException.class, () -> target("owner", "fixture/read", ""));
        assertThrows(IllegalArgumentException.class, () -> target("owner", "fixture/*", "1"));
        assertThrows(IllegalArgumentException.class, () -> requirement(SUPPORT, Set.of(" ")));
        assertThrows(IllegalArgumentException.class,
                () -> new CommercialSupportingAccessRequirement(SUPPORT, Set.of(), ""));
        assertThrows(NullPointerException.class,
                () -> new CommercialSupportingAccessRequirement(SUPPORT, null, "fixture-authority"));
        assertThrows(IllegalArgumentException.class, () -> binding(READ, TARGET, "", Set.of()));
        assertThrows(IllegalArgumentException.class, () -> new CommercialAccessBinding(READ,
                CommercialEntitlementTargetKind.OPERATION_ACCESS, TARGET, "READ", "", Set.of(), "boundary"));
        assertThrows(IllegalArgumentException.class, () -> new CommercialAccessBinding(READ,
                CommercialEntitlementTargetKind.OPERATION_ACCESS, TARGET, "READ", "authority", Set.of(), ""));
        assertThrows(IllegalArgumentException.class, () -> new CommercialCatalogueManifest(
                revision("one", Set.of(), Set.of()), Set.of(), Set.of(), "fixture-approval"));
        assertThrows(IllegalArgumentException.class, () -> new CommercialCatalogueManifest(
                revision("one", Set.of(), Set.of()), Set.of(), Set.of(" "), "fixture-approval"));
        assertThrows(IllegalArgumentException.class, () -> new CommercialCatalogueManifest(
                revision("one", Set.of(), Set.of()), Set.of(), Set.of("fixture-evidence"), ""));
    }

    @Test
    void retained_entitlement_identity_cannot_change_meaning_between_generations() {
        var old = manifest("one", Set.of(), Set.of(READ), Set.of(binding(READ, TARGET, "READ", Set.of())));
        var changed = manifest("two", Set.of(), Set.of(READ), Set.of(binding(READ, TARGET, "WRITE", Set.of())));
        assertThrows(IllegalArgumentException.class, () -> changed.requireCompatibleWith(old));
    }

    @Test
    void retained_exact_target_purpose_cannot_be_reassigned_another_entitlement() {
        var old = manifest("one", Set.of(), Set.of(READ), Set.of(binding(READ, TARGET, "READ", Set.of())));
        var changed = manifest("two", Set.of(), Set.of(VIEW), Set.of(binding(VIEW, TARGET, "READ", Set.of())));
        assertThrows(IllegalArgumentException.class, () -> changed.requireCompatibleWith(old));
    }

    @Test
    void changed_support_or_residual_boundary_cannot_replace_retained_binding_evidence() {
        var original = binding(READ, TARGET, "READ", Set.of());
        var old = manifest("one", Set.of(), Set.of(READ), Set.of(original));
        var changedSupport = binding(READ, TARGET, "READ", Set.of(requirement(SUPPORT, Set.of())));
        var changedBoundary = new CommercialAccessBinding(READ, original.targetKind(), TARGET, "READ",
                original.governingAuthority(), Set.of(), "different-residual-boundary-authority");
        for (var changed : Set.of(changedSupport, changedBoundary)) {
            var next = manifest("two", Set.of(), Set.of(READ), Set.of(changed));
            assertThrows(IllegalArgumentException.class, () -> next.requireCompatibleWith(old));
        }
    }

    @Test
    void unchanged_binding_may_be_repackaged_without_mutating_old_grants() {
        var bindings = Set.of(binding(READ, TARGET, "READ", Set.of()));
        var old = manifest("one", Set.of(), Set.of(READ), bindings);
        var changed = manifest("two", Set.of(READ), Set.of(READ), bindings);
        assertDoesNotThrow(() -> changed.requireCompatibleWith(old));
        assertTrue(old.revision().freePlan().entitlements().isEmpty());
    }

    @Test
    void retained_plan_identity_cannot_be_reused_for_changed_content() {
        var bindings = Set.of(binding(READ, TARGET, "READ", Set.of()));
        var old = manifest("one", Set.of(), Set.of(READ), bindings);
        var revisions = new StandardPlanCatalogueRevision("two",
                new StandardPlanRevision(StandardPlanLevel.FREE, "one-free", Set.of(READ)),
                new StandardPlanRevision(StandardPlanLevel.BUSINESS, "two-business", Set.of(READ)),
                new StandardPlanRevision(StandardPlanLevel.GROWTH, "two-growth", Set.of(READ)));
        var changed = new CommercialCatalogueManifest(revisions, bindings, Set.of("fixture-evidence"), "fixture-approval");
        assertThrows(IllegalArgumentException.class, () -> changed.requireCompatibleWith(old));
    }

    @Test
    void same_catalogue_identity_requires_exact_manifest_content_including_evidence() {
        var old = manifest("one", Set.of(), Set.of(), Set.of());
        assertDoesNotThrow(() -> old.requireCompatibleWith(old));
        var changed = new CommercialCatalogueManifest(old.revision(), old.bindings(),
                old.allocationConformanceEvidence(), "different-approval-reference");
        assertThrows(IllegalArgumentException.class, () -> changed.requireCompatibleWith(old));
    }

    private static Set<CommercialAccessBinding> supportedBindings() {
        return Set.of(binding(READ, TARGET, "READ", Set.of(requirement(SUPPORT, Set.of("PRESENT")))),
                binding(VIEW, SUPPORT, "PRESENT", Set.of()));
    }

    private static CommercialAccessTarget target(String owner, String identity, String revision) {
        return new CommercialAccessTarget(owner, identity, revision);
    }

    private static CommercialSupportingAccessRequirement requirement(CommercialAccessTarget target, Set<String> purposes) {
        return new CommercialSupportingAccessRequirement(target, purposes, "fixture-classification-authority");
    }

    private static CommercialAccessBinding binding(CommercialEntitlementIdentity identity, CommercialAccessTarget target,
                                                  String purpose, Set<CommercialSupportingAccessRequirement> supports) {
        return new CommercialAccessBinding(identity, CommercialEntitlementTargetKind.OPERATION_ACCESS,
                target, purpose, "fixture-target-authority", supports, "fixture-new-use-residual-authority");
    }

    private static CommercialEntitlementIdentity entitlement(String identifier) {
        return new CommercialEntitlementIdentity(identifier);
    }

    private static CommercialCatalogueManifest manifest(String id, Set<CommercialEntitlementIdentity> free,
                                                       Set<CommercialEntitlementIdentity> paid, Set<CommercialAccessBinding> bindings) {
        return new CommercialCatalogueManifest(revision(id, free, paid), bindings,
                Set.of("fixture-allocation-evidence"), "fixture-approval-reference");
    }

    private static StandardPlanCatalogueRevision revision(String id, Set<CommercialEntitlementIdentity> free,
                                                          Set<CommercialEntitlementIdentity> paid) {
        return new StandardPlanCatalogueRevision(id,
                new StandardPlanRevision(StandardPlanLevel.FREE, id + "-free", free),
                new StandardPlanRevision(StandardPlanLevel.BUSINESS, id + "-business", paid),
                new StandardPlanRevision(StandardPlanLevel.GROWTH, id + "-growth", paid));
    }
}
