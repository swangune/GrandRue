package mainstreet.inventory;

import mainstreet.semantic.compiler.ConfigurationCompiler;
import mainstreet.semantic.configuration.ConfigurationImpactClassification;
import mainstreet.semantic.configuration.ConfigurationImpactContext;
import mainstreet.semantic.configuration.ConfigurationPackageResolver;
import mainstreet.semantic.configuration.ConfigurationValidationEvidence;
import mainstreet.semantic.configuration.ConfigurationValidationOutcome;
import mainstreet.semantic.configuration.MerchantConfiguration;
import mainstreet.semantic.registry.CapabilityRelationshipType;
import mainstreet.semantic.registry.InMemorySemanticRegistry;
import mainstreet.semantic.registry.RegisteredCapability;
import mainstreet.semantic.registry.RegisteredCapabilityRelationship;
import mainstreet.semantic.registry.SemanticRegistrySnapshot;
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
 * v1.0 §§20–23 and §26 impact analysis, existing-commitment protection and capability deactivation;
 * designs/MS-PROT-058 — Inventory Authority, Stock Claims & Availability Model.md, v1.0 §§1–9;
 * designs/MS-PROT-058 v1.1 — Inventory Position, Mutation & Quantity Claim Execution Contract Amendment.md,
 * §§1–3 Inventory ownership, optionality and surviving authority boundaries.
 */
class InventoryAvailabilityImpactAssessmentTest {
    private static final Instant AT = Instant.parse("2026-09-07T22:45:00Z");
    private static final String ENABLED_EFFECT =
            "New inventory-tracked activity can use Main Street Inventory where applicable inventory requirements are satisfied.";
    private static final String DISABLED_EFFECT =
            "New inventory-tracked activity will no longer be initiated through Main Street Inventory.";

    @Test
    void initial_enablement_describes_new_inventory_activity_without_claiming_stock_or_claims_are_created() {
        var compiler = compiler(false, false);

        var result = new InventoryAvailabilityImpactAssessment(compiler)
                .assess(context(compiler, Optional.empty(), Set.of("inventory")));

        assertEquals(List.of(ENABLED_EFFECT), result.businessFacingEffects());
        assertEquals(List.of(ConfigurationImpactClassification.CONSEQUENTIAL),
                result.findings().stream().map(finding -> finding.classification()).toList());
    }

    @Test
    void deactivation_stops_new_inventory_activity_without_abandoning_existing_inventory_truth() {
        var compiler = compiler(false, false);

        var result = new InventoryAvailabilityImpactAssessment(compiler).assess(
                context(compiler, Optional.of(base(Set.of("inventory"), "old")), Set.of()));

        assertEquals(List.of(DISABLED_EFFECT), result.businessFacingEffects());
        assertEquals(List.of(ConfigurationImpactClassification.CONSEQUENTIAL),
                result.findings().stream().map(finding -> finding.classification()).toList());
    }

    @Test
    void unchanged_enabled_membership_does_not_invent_stock_position_claim_or_movement_effects() {
        var compiler = compiler(false, false);

        var result = new InventoryAvailabilityImpactAssessment(compiler).assess(
                context(compiler, Optional.of(base(Set.of("inventory"), "old")), Set.of("inventory")));

        assertTrue(result.businessFacingEffects().isEmpty());
        assertTrue(result.findings().isEmpty());
    }

    @Test
    void unrelated_capabilities_do_not_activate_inventory() {
        var compiler = compiler(false, false);

        var result = new InventoryAvailabilityImpactAssessment(compiler).assess(
                context(compiler, Optional.of(base(Set.of("publication"), "old")), Set.of("publication")));

        assertTrue(result.businessFacingEffects().isEmpty());
        assertTrue(result.findings().isEmpty());
    }

    @Test
    void uses_each_pinned_release_dependency_closure_instead_of_selected_identifiers() {
        var removed = compiler(true, false);
        var removedResult = new InventoryAvailabilityImpactAssessment(removed).assess(
                context(removed, Optional.of(base(Set.of("commerce"), "old")), Set.of("commerce")));
        assertEquals(List.of(DISABLED_EFFECT), removedResult.businessFacingEffects());

        var added = compiler(false, true);
        var addedResult = new InventoryAvailabilityImpactAssessment(added).assess(
                context(added, Optional.of(base(Set.of("commerce"), "old")), Set.of("commerce")));
        assertEquals(List.of(ENABLED_EFFECT), addedResult.businessFacingEffects());
    }

    @Test
    void missing_historical_release_does_not_become_an_inactive_base() {
        var compiler = compiler(false, false);

        assertThrows(IllegalArgumentException.class, () -> new InventoryAvailabilityImpactAssessment(compiler)
                .assess(context(compiler, Optional.of(base(Set.of("inventory"), "missing")), Set.of())));
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

    private static ConfigurationCompiler compiler(boolean oldRequiresInventory, boolean newRequiresInventory) {
        var registry = new InMemorySemanticRegistry();
        registry.publish(snapshot("old", oldRequiresInventory));
        registry.publish(snapshot("new", newRequiresInventory));
        return new ConfigurationCompiler(registry);
    }

    private static SemanticRegistrySnapshot snapshot(String release, boolean requiresInventory) {
        return new SemanticRegistrySnapshot(release,
                Set.of(new RegisteredCapability("inventory", List.of(), List.of()),
                        new RegisteredCapability("commerce", List.of(), List.of()),
                        new RegisteredCapability("publication", List.of(), List.of())),
                requiresInventory ? Set.of(new RegisteredCapabilityRelationship("commerce",
                        CapabilityRelationshipType.REQUIRES, "inventory")) : Set.of());
    }
}
