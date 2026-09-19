package grandrue.semantic.configuration;

import grandrue.application.MerchantScope;
import grandrue.booking.BookingAvailabilityImpactAssessment;
import grandrue.publication.PublicationAvailabilityImpactAssessment;
import grandrue.semantic.compiler.ConfigurationCompiler;
import grandrue.semantic.registry.*;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Authority: MS-PROT-040 v1.0,
 * designs/MS-PROT-040 — Merchant Configuration Review, Approval, Activation & Change Model.md,
 * §20 Configuration diff, §21 Impact analysis, §26 Capability deactivation;
 * MS-PROT-040 v1.3,
 * designs/MS-PROT-040 v1.3 — Configuration Validation, Impact Review & Approval Evidence Amendment.md,
 * §11 Impact-Evidence Production Predicate.
 * Exact case/owner scope: docs/development/imp-05-r4b-membership-coverage-progress-2026-09-08.md.
 */
class CapabilityMembershipImpactAssessmentTest {
    private static final Instant AT = Instant.parse("2026-09-08T16:00:00Z");
    private static final ConfigurationImpactContribution EMPTY =
            new ConfigurationImpactContribution(List.of(), List.of());

    @Test
    void initial_membership_requires_all_owners_before_any_owner_is_called() {
        var compiler = compiler(false, false);
        var calls = new AtomicInteger();
        var assessment = new CapabilityMembershipImpactAssessment(compiler,
                Map.of("booking", context -> { calls.incrementAndGet(); return EMPTY; }));

        assertThrows(IllegalStateException.class, () -> assessment.assess(
                context(compiler, Optional.empty(), Set.of("booking", "publication"))));
        assertEquals(0, calls.get());
    }

    @Test
    void removed_capability_still_requires_its_owner() {
        var compiler = compiler(false, false);
        var assessment = new CapabilityMembershipImpactAssessment(compiler, Map.of());

        assertThrows(IllegalStateException.class, () -> assessment.assess(
                context(compiler, Optional.of(base(Set.of("publication"), "old")), Set.of())));
    }

    @Test
    void dependency_changes_in_either_direction_require_coverage_even_with_identical_selections() {
        for (boolean wasRequired : List.of(false, true)) {
            var compiler = compiler(wasRequired, !wasRequired);
            var calls = new AtomicInteger();
            var assessment = new CapabilityMembershipImpactAssessment(compiler,
                    Map.of("content", context -> { calls.incrementAndGet(); return EMPTY; }));

            assertThrows(IllegalStateException.class, () -> assessment.assess(
                    context(compiler, Optional.of(base(Set.of("content"), "old")), Set.of("content"))));
            assertEquals(0, calls.get());
        }
    }

    @Test
    void redundant_direct_selection_removal_does_not_invent_missing_membership_coverage() {
        var compiler = compiler(true, true);
        var result = new CapabilityMembershipImpactAssessment(compiler, Map.of()).assess(
                context(compiler, Optional.of(base(Set.of("content", "publication"), "old")), Set.of("content")));

        assertEquals(EMPTY, result);
    }

    @Test
    void unchanged_disabled_booking_owner_still_assesses_residual_obligations() {
        var compiler = compiler(false, false);
        var booking = new BookingAvailabilityImpactAssessment(compiler, scope -> {
            assertEquals(new MerchantScope("merchant-a"), scope);
            return true;
        });

        var result = new CapabilityMembershipImpactAssessment(compiler, Map.of("booking", booking))
                .assess(context(compiler, Optional.of(base(Set.of(), "old")), Set.of()));

        assertEquals(List.of(), result.businessFacingEffects());
        assertEquals(List.of(new ConfigurationImpactFinding(ConfigurationImpactClassification.INFORMATIONAL,
                "Some existing bookings still require management; disabling new bookings does not cancel or discharge them.")),
                result.findings());
    }

    @Test
    void actual_publication_owner_supplies_effects_without_generic_identifier_derived_text() {
        var compiler = compiler(false, false);
        var owner = new PublicationAvailabilityImpactAssessment(compiler);
        var context = context(compiler, Optional.empty(), Set.of("publication"));

        var result = new CapabilityMembershipImpactAssessment(compiler, Map.of("publication", owner)).assess(context);

        assertEquals(owner.assess(context), result);
        assertFalse(result.businessFacingEffects().isEmpty());
    }

    @Test
    void unavailable_history_fails_before_any_owner_is_called() {
        var compiler = compiler(false, false);
        ConfigurationImpactAssessment owner = mock(ConfigurationImpactAssessment.class);
        var assessment = new CapabilityMembershipImpactAssessment(compiler, Map.of("publication", owner));

        assertThrows(IllegalArgumentException.class, () -> assessment.assess(
                context(compiler, Optional.of(base(Set.of("publication"), "missing")), Set.of())));
        verifyNoInteractions(owner);
    }

    @Test
    void owner_order_is_stable_and_exact_context_and_classified_findings_are_preserved() {
        var compiler = compiler(false, false);
        var context = context(compiler, Optional.empty(), Set.of("booking", "publication"));
        var calls = new ArrayList<String>();
        var finding = new ConfigurationImpactFinding(ConfigurationImpactClassification.EXISTING_COMMITMENT_CONFLICT,
                "Owner-reported fixture conflict");
        var owners = new LinkedHashMap<String, ConfigurationImpactAssessment>();
        owners.put("publication", input -> {
            assertSame(context, input);
            calls.add("publication");
            return new ConfigurationImpactContribution(List.of("Publication fixture effect"), List.of());
        });
        owners.put("booking", input -> {
            assertSame(context, input);
            calls.add("booking");
            return new ConfigurationImpactContribution(List.of("Booking fixture effect"), List.of(finding));
        });

        var result = new CapabilityMembershipImpactAssessment(compiler, owners).assess(context);

        assertEquals(List.of("booking", "publication"), calls);
        assertEquals(List.of("Booking fixture effect", "Publication fixture effect"), result.businessFacingEffects());
        assertEquals(List.of(finding), result.findings());
    }

    @Test
    void unavailable_or_null_owner_result_cannot_become_an_empty_contribution() {
        var compiler = compiler(false, false);
        var context = context(compiler, Optional.empty(), Set.of("publication"));
        var failure = new IllegalStateException("Owner unavailable");

        assertSame(failure, assertThrows(IllegalStateException.class,
                () -> new CapabilityMembershipImpactAssessment(compiler,
                        Map.of("publication", input -> { throw failure; })).assess(context)));
        assertThrows(NullPointerException.class,
                () -> new CapabilityMembershipImpactAssessment(compiler,
                        Map.of("publication", input -> null)).assess(context));
    }

    @Test
    void coverage_registration_is_immutable_and_rejects_blank_owner_identity() {
        var compiler = compiler(false, false);
        var owners = new HashMap<String, ConfigurationImpactAssessment>();
        owners.put("publication", input -> EMPTY);
        var assessment = new CapabilityMembershipImpactAssessment(compiler, owners);
        owners.clear();

        assertEquals(EMPTY, assessment.assess(context(compiler, Optional.empty(), Set.of("publication"))));
        assertThrows(IllegalArgumentException.class,
                () -> new CapabilityMembershipImpactAssessment(compiler, Map.of(" ", input -> EMPTY)));
    }

    @Test
    void another_assessment_effect_cannot_hide_missing_membership_coverage_from_analyzer() {
        var compiler = compiler(false, false);
        var context = context(compiler, Optional.empty(), Set.of("publication"));
        var scope = new MerchantScope("merchant-a");
        var revisions = mock(ConfigurationRevisionAuthority.class);
        var validations = mock(ConfigurationValidationEvidenceAuthority.class);
        when(revisions.configuration(scope, "c2")).thenReturn(Optional.of(context.candidate()));
        when(validations.evidence(scope, "v1")).thenReturn(Optional.of(context.validation()));
        var analyzer = new ConfigurationImpactAnalyzer(revisions, validations, List.of(
                input -> new ConfigurationImpactContribution(List.of("Other fixture effect"), List.of()),
                new CapabilityMembershipImpactAssessment(compiler, Map.of())));

        assertThrows(IllegalStateException.class,
                () -> analyzer.analyze(scope, "v1", "p1", context.resolvedPackage(), AT));
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
                        new RegisteredCapability("booking", List.of(), List.of())),
                requiresPublication ? Set.of(new RegisteredCapabilityRelationship("content",
                        CapabilityRelationshipType.REQUIRES, "publication")) : Set.of());
    }
}
