package grandrue.scheduling;

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
 * v1.0 §§20–30 configuration impact, existing-commitment protection and Scheduling change semantics;
 * designs/MS-PROT-042 v1.6 — Production Scheduling Evidence, Appointment Admission & Customer Surface Requirement Amendment.md,
 * §§1–4 Scheduling-owned current Appointment Scheduling Evaluation and non-persistent availability boundary;
 * designs/MS-PROT-050 v1.1 — Business Operating Hours, Scheduling-Time & Digital Contactability Amendment.md,
 * §§1–4 separation of Business Hours, Scheduling constraints, staff/resource availability and Enquiry.
 */
class SchedulingAvailabilityImpactAssessmentTest {
    private static final Instant AT = Instant.parse("2026-09-08T03:45:00Z");
    private static final String ENABLED_EFFECT =
            "New appointment scheduling activity can use Main Street Scheduling where applicable scheduling semantics and authority permit it.";
    private static final String DISABLED_EFFECT =
            "New appointment scheduling activity will no longer be initiated through Main Street Scheduling.";

    @Test
    void initial_enablement_describes_new_scheduling_activity_without_claiming_current_schedulability() {
        var compiler = compiler(false, false);

        var result = new SchedulingAvailabilityImpactAssessment(compiler)
                .assess(context(compiler, Optional.empty(), Set.of("scheduling")));

        assertEquals(List.of(ENABLED_EFFECT), result.businessFacingEffects());
        assertEquals(List.of(ConfigurationImpactClassification.CONSEQUENTIAL),
                result.findings().stream().map(finding -> finding.classification()).toList());
    }

    @Test
    void deactivation_stops_new_scheduling_activity_without_rewriting_existing_appointment_truth() {
        var compiler = compiler(false, false);

        var result = new SchedulingAvailabilityImpactAssessment(compiler).assess(
                context(compiler, Optional.of(base(Set.of("scheduling"), "old")), Set.of()));

        assertEquals(List.of(DISABLED_EFFECT), result.businessFacingEffects());
        assertEquals(List.of(ConfigurationImpactClassification.CONSEQUENTIAL),
                result.findings().stream().map(finding -> finding.classification()).toList());
    }

    @Test
    void unchanged_enabled_membership_does_not_invent_hours_staff_resource_capacity_or_slot_effects() {
        var compiler = compiler(false, false);

        var result = new SchedulingAvailabilityImpactAssessment(compiler).assess(
                context(compiler, Optional.of(base(Set.of("scheduling"), "old")), Set.of("scheduling")));

        assertTrue(result.businessFacingEffects().isEmpty());
        assertTrue(result.findings().isEmpty());
    }

    @Test
    void unrelated_capabilities_do_not_activate_scheduling() {
        var compiler = compiler(false, false);

        var result = new SchedulingAvailabilityImpactAssessment(compiler).assess(
                context(compiler, Optional.of(base(Set.of("publication"), "old")), Set.of("publication")));

        assertTrue(result.businessFacingEffects().isEmpty());
        assertTrue(result.findings().isEmpty());
    }

    @Test
    void uses_each_pinned_release_dependency_closure_instead_of_selected_identifiers() {
        var removed = compiler(true, false);
        var removedResult = new SchedulingAvailabilityImpactAssessment(removed).assess(
                context(removed, Optional.of(base(Set.of("appointment"), "old")), Set.of("appointment")));
        assertEquals(List.of(DISABLED_EFFECT), removedResult.businessFacingEffects());

        var added = compiler(false, true);
        var addedResult = new SchedulingAvailabilityImpactAssessment(added).assess(
                context(added, Optional.of(base(Set.of("appointment"), "old")), Set.of("appointment")));
        assertEquals(List.of(ENABLED_EFFECT), addedResult.businessFacingEffects());
    }

    @Test
    void missing_historical_release_does_not_become_an_inactive_base() {
        var compiler = compiler(false, false);

        assertThrows(IllegalArgumentException.class, () -> new SchedulingAvailabilityImpactAssessment(compiler)
                .assess(context(compiler, Optional.of(base(Set.of("scheduling"), "missing")), Set.of())));
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

    private static ConfigurationCompiler compiler(boolean oldRequiresScheduling, boolean newRequiresScheduling) {
        var registry = new InMemorySemanticRegistry();
        registry.publish(snapshot("old", oldRequiresScheduling));
        registry.publish(snapshot("new", newRequiresScheduling));
        return new ConfigurationCompiler(registry);
    }

    private static SemanticRegistrySnapshot snapshot(String release, boolean requiresScheduling) {
        return new SemanticRegistrySnapshot(release,
                Set.of(new RegisteredCapability("scheduling", List.of(), List.of()),
                        new RegisteredCapability("appointment", List.of(), List.of()),
                        new RegisteredCapability("publication", List.of(), List.of())),
                requiresScheduling ? Set.of(new RegisteredCapabilityRelationship("appointment",
                        CapabilityRelationshipType.REQUIRES, "scheduling")) : Set.of());
    }
}
