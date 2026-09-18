package grandrue.ordering;

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
 * composite designs/MS-PROT-077 through v1.1 — Order Commitment, Amendment & Lifecycle Model,
 * Ordering ownership, independent capability activation and historical Order-commitment boundaries.
 */
class OrderingAvailabilityImpactAssessmentTest {
    private static final Instant AT = Instant.parse("2026-09-07T20:30:00Z");
    private static final String ENABLED_EFFECT =
            "New orders can be accepted through Main Street where applicable ordering requirements are satisfied.";
    private static final String DISABLED_EFFECT =
            "New orders will no longer be accepted through Main Street.";

    @Test
    void initial_enablement_describes_new_order_activity_without_claiming_commitment_or_fulfilment() {
        var compiler = compiler(false, false);

        var result = new OrderingAvailabilityImpactAssessment(compiler)
                .assess(context(compiler, Optional.empty(), Set.of("ordering")));

        assertEquals(List.of(ENABLED_EFFECT), result.businessFacingEffects());
        assertEquals(List.of(ConfigurationImpactClassification.CONSEQUENTIAL),
                result.findings().stream().map(finding -> finding.classification()).toList());
    }

    @Test
    void deactivation_stops_new_order_activity_without_claiming_existing_orders_are_cancelled() {
        var compiler = compiler(false, false);

        var result = new OrderingAvailabilityImpactAssessment(compiler).assess(
                context(compiler, Optional.of(base(Set.of("ordering"), "old")), Set.of()));

        assertEquals(List.of(DISABLED_EFFECT), result.businessFacingEffects());
        assertEquals(List.of(ConfigurationImpactClassification.CONSEQUENTIAL),
                result.findings().stream().map(finding -> finding.classification()).toList());
    }

    @Test
    void unchanged_enabled_membership_does_not_invent_order_policy_or_commitment_effects() {
        var compiler = compiler(false, false);

        var result = new OrderingAvailabilityImpactAssessment(compiler).assess(
                context(compiler, Optional.of(base(Set.of("ordering"), "old")), Set.of("ordering")));

        assertTrue(result.businessFacingEffects().isEmpty());
        assertTrue(result.findings().isEmpty());
    }

    @Test
    void unrelated_capabilities_do_not_activate_ordering() {
        var compiler = compiler(false, false);

        var result = new OrderingAvailabilityImpactAssessment(compiler).assess(
                context(compiler, Optional.of(base(Set.of("publication"), "old")), Set.of("publication")));

        assertTrue(result.businessFacingEffects().isEmpty());
        assertTrue(result.findings().isEmpty());
    }

    @Test
    void uses_each_pinned_release_dependency_closure_instead_of_selected_identifiers() {
        var removed = compiler(true, false);
        var removedResult = new OrderingAvailabilityImpactAssessment(removed).assess(
                context(removed, Optional.of(base(Set.of("commerce"), "old")), Set.of("commerce")));
        assertEquals(List.of(DISABLED_EFFECT), removedResult.businessFacingEffects());

        var added = compiler(false, true);
        var addedResult = new OrderingAvailabilityImpactAssessment(added).assess(
                context(added, Optional.of(base(Set.of("commerce"), "old")), Set.of("commerce")));
        assertEquals(List.of(ENABLED_EFFECT), addedResult.businessFacingEffects());
    }

    @Test
    void missing_historical_release_does_not_become_an_inactive_base() {
        var compiler = compiler(false, false);

        assertThrows(IllegalArgumentException.class, () -> new OrderingAvailabilityImpactAssessment(compiler)
                .assess(context(compiler, Optional.of(base(Set.of("ordering"), "missing")), Set.of())));
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

    private static ConfigurationCompiler compiler(boolean oldRequiresOrdering, boolean newRequiresOrdering) {
        var registry = new InMemorySemanticRegistry();
        registry.publish(snapshot("old", oldRequiresOrdering));
        registry.publish(snapshot("new", newRequiresOrdering));
        return new ConfigurationCompiler(registry);
    }

    private static SemanticRegistrySnapshot snapshot(String release, boolean requiresOrdering) {
        return new SemanticRegistrySnapshot(release,
                Set.of(new RegisteredCapability("ordering", List.of(), List.of()),
                        new RegisteredCapability("commerce", List.of(), List.of()),
                        new RegisteredCapability("publication", List.of(), List.of())),
                requiresOrdering ? Set.of(new RegisteredCapabilityRelationship("commerce",
                        CapabilityRelationshipType.REQUIRES, "ordering")) : Set.of());
    }
}
