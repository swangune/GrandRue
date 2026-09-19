package grandrue.booking;

import grandrue.application.MerchantScope;
import grandrue.semantic.compiler.ConfigurationCompiler;
import grandrue.semantic.configuration.*;
import grandrue.semantic.registry.*;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Authority: designs/MS-PROT-042 v1.4 — Booking Residual Obligation & Discharge Amendment.md,
 * §§1–5 Booking-owned obligations and discharge;
 * designs/MS-PROT-040 — Merchant Configuration Review, Approval, Activation & Change Model.md,
 * v1.0 §§20–24 business effects, classification and protected commitments.
 */
class BookingAvailabilityImpactAssessmentTest {
    private static final Instant AT = Instant.parse("2026-09-06T12:00:00Z");

    @Test
    void initial_enablement_describes_new_booking_activity_without_inventing_self_service() {
        var compiler = compiler(false, false);
        var owner = mock(BookingResidualObligationAuthority.class);
        var result = new BookingAvailabilityImpactAssessment(compiler, owner)
                .assess(context(compiler, Optional.empty(), Set.of("booking")));

        assertEquals(List.of("New bookings will be enabled, subject to the configured booking policies."),
                result.businessFacingEffects());
        assertEquals(ConfigurationImpactClassification.CONSEQUENTIAL, result.findings().getFirst().classification());
        verifyNoInteractions(owner);
    }

    @Test
    void disabling_new_bookings_preserves_owner_confirmed_existing_obligations() {
        var compiler = compiler(false, false);
        var owner = mock(BookingResidualObligationAuthority.class);
        when(owner.hasOutstandingBookingObligation(new MerchantScope("merchant-a"))).thenReturn(true);
        var result = new BookingAvailabilityImpactAssessment(compiler, owner).assess(
                context(compiler, Optional.of(base(Set.of("booking"), "old")), Set.of()));

        assertEquals(List.of("New bookings will no longer be enabled."), result.businessFacingEffects());
        assertEquals(List.of(ConfigurationImpactClassification.CONSEQUENTIAL, ConfigurationImpactClassification.INFORMATIONAL),
                result.findings().stream().map(ConfigurationImpactFinding::classification).toList());
        assertEquals("Some existing bookings still require management; disabling new bookings does not cancel or discharge them.",
                result.findings().getLast().businessFacingDescription());
        verify(owner).hasOutstandingBookingObligation(new MerchantScope("merchant-a"));
    }

    @Test
    void already_disabled_booking_still_checks_residual_obligations() {
        var compiler = compiler(false, false);
        var result = new BookingAvailabilityImpactAssessment(compiler, scope -> true).assess(
                context(compiler, Optional.of(base(Set.of(), "old")), Set.of()));

        assertTrue(result.businessFacingEffects().isEmpty());
        assertEquals(1, result.findings().size());
        assertEquals(ConfigurationImpactClassification.INFORMATIONAL, result.findings().getFirst().classification());
    }

    @Test
    void unchanged_enabled_membership_is_not_a_claim_about_policy_changes() {
        var compiler = compiler(false, false);
        var owner = mock(BookingResidualObligationAuthority.class);
        var result = new BookingAvailabilityImpactAssessment(compiler, owner).assess(
                context(compiler, Optional.of(base(Set.of("booking"), "old")), Set.of("booking")));

        assertTrue(result.businessFacingEffects().isEmpty());
        assertTrue(result.findings().isEmpty());
        verifyNoInteractions(owner);
    }

    @Test
    void uses_each_pinned_release_dependency_closure_instead_of_selected_identifiers() {
        var removed = compiler(true, false);
        var removedResult = new BookingAvailabilityImpactAssessment(removed, scope -> false).assess(
                context(removed, Optional.of(base(Set.of("portfolio"), "old")), Set.of("portfolio")));
        assertEquals(List.of("New bookings will no longer be enabled."), removedResult.businessFacingEffects());

        var added = compiler(false, true);
        var addedResult = new BookingAvailabilityImpactAssessment(added, scope -> false).assess(
                context(added, Optional.of(base(Set.of("portfolio"), "old")), Set.of("portfolio")));
        assertEquals(List.of("New bookings will be enabled, subject to the configured booking policies."),
                addedResult.businessFacingEffects());
    }

    @Test
    void missing_historical_release_does_not_become_an_inactive_base() {
        var compiler = compiler(false, false);
        var owner = mock(BookingResidualObligationAuthority.class);

        assertThrows(IllegalArgumentException.class, () -> new BookingAvailabilityImpactAssessment(compiler, owner)
                .assess(context(compiler, Optional.of(base(Set.of("booking"), "missing")), Set.of())));
        verifyNoInteractions(owner);
    }

    @Test
    void unavailable_owner_is_not_treated_as_no_outstanding_bookings() {
        var compiler = compiler(false, false);
        var failure = new IllegalStateException("Booking owner unavailable");
        var assessment = new BookingAvailabilityImpactAssessment(compiler, scope -> { throw failure; });

        assertSame(failure, assertThrows(IllegalStateException.class,
                () -> assessment.assess(context(compiler, Optional.empty(), Set.of()))));
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

    private static ConfigurationCompiler compiler(boolean oldRequiresBooking, boolean newRequiresBooking) {
        var registry = new InMemorySemanticRegistry();
        registry.publish(snapshot("old", oldRequiresBooking));
        registry.publish(snapshot("new", newRequiresBooking));
        return new ConfigurationCompiler(registry);
    }

    private static SemanticRegistrySnapshot snapshot(String release, boolean requiresBooking) {
        return new SemanticRegistrySnapshot(release,
                Set.of(new RegisteredCapability("booking", List.of(), List.of()),
                        new RegisteredCapability("portfolio", List.of(), List.of())),
                requiresBooking ? Set.of(new RegisteredCapabilityRelationship("portfolio",
                        CapabilityRelationshipType.REQUIRES, "booking")) : Set.of());
    }
}
