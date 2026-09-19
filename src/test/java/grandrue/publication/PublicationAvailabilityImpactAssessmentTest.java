package grandrue.publication;

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
 * Exact authority and case mapping:
 * docs/development/imp-05-r4b-publication-impact-progress-2026-09-08.md, Accepted authority.
 */
class PublicationAvailabilityImpactAssessmentTest {
    private static final Instant AT = Instant.parse("2026-09-08T03:45:00Z");
    private static final String ENABLED_EFFECT =
            "New publishing activity can use GrandRue Publication where applicable authority permits it.";
    private static final String DISABLED_EFFECT =
            "New publishing activity will no longer be initiated through GrandRue Publication.";

    @Test
    void initial_enablement_describes_new_publication_activity_without_claiming_public_visibility() {
        var compiler = compiler(false, false);

        var result = new PublicationAvailabilityImpactAssessment(compiler)
                .assess(context(compiler, Optional.empty(), Set.of("publication")));

        assertEquals(List.of(ENABLED_EFFECT), result.businessFacingEffects());
        assertEquals(List.of(ConfigurationImpactClassification.CONSEQUENTIAL),
                result.findings().stream().map(finding -> finding.classification()).toList());
    }

    @Test
    void deactivation_stops_new_publication_activity_without_rewriting_existing_publication_truth() {
        var compiler = compiler(false, false);

        var result = new PublicationAvailabilityImpactAssessment(compiler).assess(
                context(compiler, Optional.of(base(Set.of("publication"), "old")), Set.of()));

        assertEquals(List.of(DISABLED_EFFECT), result.businessFacingEffects());
        assertEquals(List.of(ConfigurationImpactClassification.CONSEQUENTIAL),
                result.findings().stream().map(finding -> finding.classification()).toList());
    }

    @Test
    void unchanged_enabled_membership_does_not_invent_visibility_or_lifecycle_effects() {
        var compiler = compiler(false, false);

        var result = new PublicationAvailabilityImpactAssessment(compiler).assess(
                context(compiler, Optional.of(base(Set.of("publication"), "old")), Set.of("publication")));

        assertTrue(result.businessFacingEffects().isEmpty());
        assertTrue(result.findings().isEmpty());
    }

    @Test
    void unrelated_capabilities_do_not_activate_publication() {
        var compiler = compiler(false, false);

        var result = new PublicationAvailabilityImpactAssessment(compiler).assess(
                context(compiler, Optional.of(base(Set.of("enquiry"), "old")), Set.of("enquiry")));

        assertTrue(result.businessFacingEffects().isEmpty());
        assertTrue(result.findings().isEmpty());
    }

    @Test
    void uses_each_pinned_release_dependency_closure_instead_of_selected_identifiers() {
        var removed = compiler(true, false);
        var removedResult = new PublicationAvailabilityImpactAssessment(removed).assess(
                context(removed, Optional.of(base(Set.of("content"), "old")), Set.of("content")));
        assertEquals(List.of(DISABLED_EFFECT), removedResult.businessFacingEffects());

        var added = compiler(false, true);
        var addedResult = new PublicationAvailabilityImpactAssessment(added).assess(
                context(added, Optional.of(base(Set.of("content"), "old")), Set.of("content")));
        assertEquals(List.of(ENABLED_EFFECT), addedResult.businessFacingEffects());
    }

    @Test
    void missing_historical_release_does_not_become_an_inactive_base() {
        var compiler = compiler(false, false);

        assertThrows(IllegalArgumentException.class, () -> new PublicationAvailabilityImpactAssessment(compiler)
                .assess(context(compiler, Optional.of(base(Set.of("publication"), "missing")), Set.of())));
    }

    @Test
    void removing_direct_selection_does_not_disable_a_still_required_publication_capability() {
        var compiler = compiler(true, true);

        var result = new PublicationAvailabilityImpactAssessment(compiler).assess(
                context(compiler, Optional.of(base(Set.of("publication", "content"), "old")), Set.of("content")));

        assertTrue(result.businessFacingEffects().isEmpty());
        assertTrue(result.findings().isEmpty());
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

    private static ConfigurationCompiler compiler(boolean oldRequiresPublication, boolean newRequiresPublication) {
        var registry = new InMemorySemanticRegistry();
        registry.publish(snapshot("old", oldRequiresPublication));
        registry.publish(snapshot("new", newRequiresPublication));
        return new ConfigurationCompiler(registry);
    }

    private static SemanticRegistrySnapshot snapshot(String release, boolean requiresPublication) {
        return new SemanticRegistrySnapshot(release,
                Set.of(new RegisteredCapability("publication", List.of(), List.of()),
                        new RegisteredCapability("content", List.of(), List.of()),
                        new RegisteredCapability("enquiry", List.of(), List.of())),
                requiresPublication ? Set.of(new RegisteredCapabilityRelationship("content",
                        CapabilityRelationshipType.REQUIRES, "publication")) : Set.of());
    }
}
