package grandrue.enquiry;

import grandrue.semantic.compiler.ConfigurationCompiler;
import grandrue.semantic.configuration.ConfigurationImpactClassification;
import grandrue.semantic.configuration.ConfigurationImpactContext;
import grandrue.semantic.configuration.ConfigurationPackageResolver;
import grandrue.semantic.configuration.ConfigurationValidationEvidence;
import grandrue.semantic.configuration.ConfigurationValidationOutcome;
import grandrue.semantic.configuration.MerchantConfiguration;
import grandrue.semantic.registry.CapabilityRelationshipType;
import grandrue.semantic.registry.InMemorySemanticRegistry;
import grandrue.semantic.registry.RegisteredCapability;
import grandrue.semantic.registry.RegisteredCapabilityRelationship;
import grandrue.semantic.registry.SemanticRegistrySnapshot;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Authority: designs/MS-PROT-040 — Merchant Configuration Review, Approval, Activation & Change Model.md,
 * v1.0 §§20–22 impact analysis/classification and §26 capability deactivation;
 * designs/MS-PROT-043 v1.4 — Production Enquiry Submission, Provenance & Merchant Observation Amendment.md,
 * §§2–6 canonical submission, merchant-general/subject-specific Enquiry and current-participation revalidation;
 * designs/MS-PROT-049 v1.2 — Public Interaction Subject Participation & Binding Amendment.md,
 * §§4, 17–18 interaction applicability, execution revalidation and stale-binding behaviour.
 */
class EnquiryAvailabilityImpactAssessmentTest {
    private static final Instant AT = Instant.parse("2026-09-06T14:00:00Z");
    private static final String ENABLED_EFFECT =
            "New customer enquiries can be accepted through GrandRue where an applicable enquiry interaction is available.";
    private static final String DISABLED_EFFECT =
            "New customer enquiries will no longer be accepted through GrandRue.";

    @Test
    void initial_enablement_describes_new_enquiry_activity_without_claiming_subject_binding_or_availability() {
        var compiler = compiler(false, false);

        var result = new EnquiryAvailabilityImpactAssessment(compiler)
                .assess(context(compiler, Optional.empty(), Set.of("enquiry")));

        assertEquals(List.of(ENABLED_EFFECT), result.businessFacingEffects());
        assertEquals(List.of(ConfigurationImpactClassification.CONSEQUENTIAL),
                result.findings().stream().map(finding -> finding.classification()).toList());
    }

    @Test
    void deactivation_stops_new_enquiry_activity_without_claiming_existing_history_is_deleted() {
        var compiler = compiler(false, false);

        var result = new EnquiryAvailabilityImpactAssessment(compiler).assess(
                context(compiler, Optional.of(base(Set.of("enquiry"), "old")), Set.of()));

        assertEquals(List.of(DISABLED_EFFECT), result.businessFacingEffects());
        assertEquals(List.of(ConfigurationImpactClassification.CONSEQUENTIAL),
                result.findings().stream().map(finding -> finding.classification()).toList());
    }

    @Test
    void unchanged_enabled_membership_does_not_invent_binding_or_submission_effects() {
        var compiler = compiler(false, false);

        var result = new EnquiryAvailabilityImpactAssessment(compiler).assess(
                context(compiler, Optional.of(base(Set.of("enquiry"), "old")), Set.of("enquiry")));

        assertTrue(result.businessFacingEffects().isEmpty());
        assertTrue(result.findings().isEmpty());
    }

    @Test
    void uses_each_pinned_release_dependency_closure_instead_of_selected_identifiers() {
        var removed = compiler(true, false);
        var removedResult = new EnquiryAvailabilityImpactAssessment(removed).assess(
                context(removed, Optional.of(base(Set.of("portfolio"), "old")), Set.of("portfolio")));
        assertEquals(List.of(DISABLED_EFFECT), removedResult.businessFacingEffects());

        var added = compiler(false, true);
        var addedResult = new EnquiryAvailabilityImpactAssessment(added).assess(
                context(added, Optional.of(base(Set.of("portfolio"), "old")), Set.of("portfolio")));
        assertEquals(List.of(ENABLED_EFFECT), addedResult.businessFacingEffects());
    }

    @Test
    void missing_historical_release_does_not_become_an_inactive_base() {
        var compiler = compiler(false, false);

        assertThrows(IllegalArgumentException.class, () -> new EnquiryAvailabilityImpactAssessment(compiler)
                .assess(context(compiler, Optional.of(base(Set.of("enquiry"), "missing")), Set.of())));
    }

    private static MerchantConfiguration base(Set<String> selected, String release) {
        return new MerchantConfiguration("merchant-a", "c1", 1, release, selected);
    }

    private static ConfigurationImpactContext context(ConfigurationCompiler compiler,
                                                      Optional<MerchantConfiguration> base, Set<String> selected) {
        var candidate = new MerchantConfiguration("merchant-a", "c2", base.isPresent() ? 2 : 1, "new",
                selected, Set.of(), base.map(MerchantConfiguration::configurationIdentifier), Optional.empty());
        var resolved = new ConfigurationPackageResolver(compiler).resolve(candidate, "compiler", AT);
        var validation = new ConfigurationValidationEvidence("v1", "merchant-a", "c2", "new", "p1",
                ConfigurationValidationOutcome.SUCCEEDED, "compiler", AT, AT);
        return new ConfigurationImpactContext(candidate, base, validation, resolved);
    }

    private static ConfigurationCompiler compiler(boolean oldRequiresEnquiry, boolean newRequiresEnquiry) {
        var registry = new InMemorySemanticRegistry();
        registry.publish(snapshot("old", oldRequiresEnquiry));
        registry.publish(snapshot("new", newRequiresEnquiry));
        return new ConfigurationCompiler(registry);
    }

    private static SemanticRegistrySnapshot snapshot(String release, boolean requiresEnquiry) {
        return new SemanticRegistrySnapshot(release,
                Set.of(new RegisteredCapability("enquiry", List.of(), List.of()),
                        new RegisteredCapability("portfolio", List.of(), List.of())),
                requiresEnquiry ? Set.of(new RegisteredCapabilityRelationship("portfolio",
                        CapabilityRelationshipType.REQUIRES, "enquiry")) : Set.of());
    }
}
