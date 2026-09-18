package grandrue.money;

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
 * v1.0 §§20–28 impact analysis, existing-commitment protection and capability deactivation;
 * designs/MS-PROT-055 — Money, Commercial Terms, Payment Obligations & Payment Evidence Model.md,
 * v1.0 §§18–26 committed commercial truth, Payment Obligation and provider-payment boundaries;
 * designs/MS-PROT-055 v1.1 — Payment Obligation, Provider Execution, Reconciliation & Refund Execution Contract Amendment.md,
 * §§1–5 and §§14–25 Payment ownership, immutable obligation truth and execution boundaries.
 */
class PaymentAvailabilityImpactAssessmentTest {
    private static final Instant AT = Instant.parse("2026-09-08T02:30:00Z");
    private static final String ENABLED_EFFECT =
            "New payment activity can use Main Street Payment where applicable payment semantics and authority permit it.";
    private static final String DISABLED_EFFECT =
            "New payment activity will no longer be initiated through Main Street Payment.";

    @Test
    void initial_enablement_describes_new_payment_activity_without_claiming_obligation_or_provider_execution() {
        var compiler = compiler(false, false);

        var result = new PaymentAvailabilityImpactAssessment(compiler)
                .assess(context(compiler, Optional.empty(), Set.of("payment")));

        assertEquals(List.of(ENABLED_EFFECT), result.businessFacingEffects());
        assertEquals(List.of(ConfigurationImpactClassification.CONSEQUENTIAL),
                result.findings().stream().map(finding -> finding.classification()).toList());
    }

    @Test
    void deactivation_stops_new_payment_activity_without_abandoning_existing_payment_truth() {
        var compiler = compiler(false, false);

        var result = new PaymentAvailabilityImpactAssessment(compiler).assess(
                context(compiler, Optional.of(base(Set.of("payment"), "old")), Set.of()));

        assertEquals(List.of(DISABLED_EFFECT), result.businessFacingEffects());
        assertEquals(List.of(ConfigurationImpactClassification.CONSEQUENTIAL),
                result.findings().stream().map(finding -> finding.classification()).toList());
    }

    @Test
    void unchanged_enabled_membership_does_not_invent_obligation_execution_evidence_or_refund_effects() {
        var compiler = compiler(false, false);

        var result = new PaymentAvailabilityImpactAssessment(compiler).assess(
                context(compiler, Optional.of(base(Set.of("payment"), "old")), Set.of("payment")));

        assertTrue(result.businessFacingEffects().isEmpty());
        assertTrue(result.findings().isEmpty());
    }

    @Test
    void unrelated_capabilities_do_not_activate_payment() {
        var compiler = compiler(false, false);

        var result = new PaymentAvailabilityImpactAssessment(compiler).assess(
                context(compiler, Optional.of(base(Set.of("publication"), "old")), Set.of("publication")));

        assertTrue(result.businessFacingEffects().isEmpty());
        assertTrue(result.findings().isEmpty());
    }

    @Test
    void uses_each_pinned_release_dependency_closure_instead_of_selected_identifiers() {
        var removed = compiler(true, false);
        var removedResult = new PaymentAvailabilityImpactAssessment(removed).assess(
                context(removed, Optional.of(base(Set.of("commerce"), "old")), Set.of("commerce")));
        assertEquals(List.of(DISABLED_EFFECT), removedResult.businessFacingEffects());

        var added = compiler(false, true);
        var addedResult = new PaymentAvailabilityImpactAssessment(added).assess(
                context(added, Optional.of(base(Set.of("commerce"), "old")), Set.of("commerce")));
        assertEquals(List.of(ENABLED_EFFECT), addedResult.businessFacingEffects());
    }

    @Test
    void missing_historical_release_does_not_become_an_inactive_base() {
        var compiler = compiler(false, false);

        assertThrows(IllegalArgumentException.class, () -> new PaymentAvailabilityImpactAssessment(compiler)
                .assess(context(compiler, Optional.of(base(Set.of("payment"), "missing")), Set.of())));
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

    private static ConfigurationCompiler compiler(boolean oldRequiresPayment, boolean newRequiresPayment) {
        var registry = new InMemorySemanticRegistry();
        registry.publish(snapshot("old", oldRequiresPayment));
        registry.publish(snapshot("new", newRequiresPayment));
        return new ConfigurationCompiler(registry);
    }

    private static SemanticRegistrySnapshot snapshot(String release, boolean requiresPayment) {
        return new SemanticRegistrySnapshot(release,
                Set.of(new RegisteredCapability("payment", List.of(), List.of()),
                        new RegisteredCapability("commerce", List.of(), List.of()),
                        new RegisteredCapability("publication", List.of(), List.of())),
                requiresPayment ? Set.of(new RegisteredCapabilityRelationship("commerce",
                        CapabilityRelationshipType.REQUIRES, "payment")) : Set.of());
    }
}
