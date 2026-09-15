package mainstreet.semantic.configuration;

import mainstreet.semantic.compiler.ConfigurationCompiler;
import mainstreet.semantic.registry.*;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Authority: designs/MS-PROT-047 — Capability Configuration Contract.md, v1.0 §§4–5, §11;
 * designs/MS-PROT-040 — Merchant Configuration Review, Approval, Activation & Change Model.md,
 * v1.0 §§20–22; designs/MS-PROT-040 v1.3 — Configuration Validation, Impact Review & Approval Evidence Amendment.md,
 * §§10–11 business effects and completed exact-result analysis.
 * Policy meanings and descriptions below are fixture-owned, not production policy registration.
 */
class ResolvedPolicyImpactAssessmentTest {
    private static final Instant AT = Instant.parse("2026-09-06T13:00:00Z");
    private static final CapabilityConfigurationDecisionIdentity MODE = identity("booking", "mode");

    @Test
    void changed_release_default_is_assessed_without_any_merchant_selection_change() {
        var compiler = compiler("assisted", "automatic");
        var changes = new ArrayList<ResolvedPolicyChange>();
        var assessment = new ResolvedPolicyImpactAssessment(compiler, Map.of(MODE, (context, change) -> {
            changes.add(change);
            assertEquals("old", context.base().orElseThrow().semanticRegistryVersion());
            assertEquals("new", context.candidate().semanticRegistryVersion());
            return effect("Customers receive an automatic response under the proposed policy.");
        }));

        var result = assessment.assess(context(compiler, Optional.of(base(Set.of("booking"), Set.of())),
                Set.of("booking"), Set.of()));

        assertEquals(List.of(new ResolvedPolicyChange(MODE, value("old", "assisted"), value("new", "automatic"))), changes);
        assertEquals(List.of("Customers receive an automatic response under the proposed policy."), result.businessFacingEffects());
    }

    @Test
    void selecting_the_existing_default_does_not_invent_an_effective_value_change() {
        var compiler = compiler("assisted", "assisted");
        PolicyImpactAssessment owner = mock(PolicyImpactAssessment.class);
        var result = new ResolvedPolicyImpactAssessment(compiler, Map.of(MODE, owner)).assess(
                context(compiler, Optional.of(new MerchantConfiguration("merchant-a", "c1", 1, "new", Set.of("booking"))), Set.of("booking"),
                        Set.of(new PolicySelection("booking", "mode", "assisted"))));

        assertTrue(result.businessFacingEffects().isEmpty());
        assertTrue(result.findings().isEmpty());
        verifyNoInteractions(owner);
    }

    @Test
    void initial_applicability_and_deactivation_preserve_absent_value_direction() {
        var compiler = compiler("assisted", "assisted");
        var changes = new ArrayList<ResolvedPolicyChange>();
        var assessment = new ResolvedPolicyImpactAssessment(compiler, Map.of(MODE, (context, change) -> {
            changes.add(change);
            return effect("The booking response policy changes for new activity.");
        }));
        assessment.assess(context(compiler, Optional.empty(), Set.of("booking"), Set.of()));
        assessment.assess(context(compiler, Optional.of(base(Set.of("booking"), Set.of())), Set.of(), Set.of()));

        assertEquals(List.of(new ResolvedPolicyChange(MODE, Optional.empty(), value("new", "assisted")),
                new ResolvedPolicyChange(MODE, value("old", "assisted"), Optional.empty())), changes);
    }

    @Test
    void equal_values_across_different_releases_still_require_owner_assessment() {
        var compiler = compiler("assisted", "assisted");
        var changes = new ArrayList<ResolvedPolicyChange>();
        var assessment = new ResolvedPolicyImpactAssessment(compiler, Map.of(MODE, (context, change) -> {
            changes.add(change);
            return effect("The owner has reviewed the response policy under the proposed release.");
        }));
        assessment.assess(context(compiler, Optional.of(base(Set.of("booking"), Set.of())), Set.of("booking"), Set.of()));

        assertEquals(List.of(new ResolvedPolicyChange(MODE, value("old", "assisted"), value("new", "assisted"))), changes);
    }

    @Test
    void coverage_is_checked_for_all_changes_before_calling_any_owner() {
        var compiler = compiler("assisted", "automatic");
        PolicyImpactAssessment booking = mock(PolicyImpactAssessment.class);
        var assessment = new ResolvedPolicyImpactAssessment(compiler, Map.of(MODE, booking));

        assertThrows(IllegalStateException.class, () -> assessment.assess(
                context(compiler, Optional.empty(), Set.of("booking", "enquiry"), Set.of())));
        verifyNoInteractions(booking);
    }

    @Test
    void same_policy_name_in_different_capabilities_is_assessed_separately_and_in_stable_order() {
        var compiler = compiler("assisted", "automatic");
        var owners = new ArrayList<String>();
        PolicyImpactAssessment owner = (context, change) -> {
            owners.add(change.identity().ownerCapabilityIdentifier());
            return effect("The proposed response policy applies to new requests.");
        };
        var assessment = new ResolvedPolicyImpactAssessment(compiler,
                Map.of(MODE, owner, identity("enquiry", "mode"), owner));

        assessment.assess(context(compiler, Optional.empty(), Set.of("enquiry", "booking"), Set.of()));

        assertEquals(List.of("booking", "enquiry"), owners);
    }

    @Test
    void empty_null_or_failed_owner_assessment_cannot_complete_policy_coverage() {
        var compiler = compiler("assisted", "automatic");
        var context = context(compiler, Optional.empty(), Set.of("booking"), Set.of());
        assertThrows(IllegalStateException.class, () -> new ResolvedPolicyImpactAssessment(compiler,
                Map.of(MODE, (input, change) -> new ConfigurationImpactContribution(List.of(), List.of()))).assess(context));
        assertThrows(NullPointerException.class, () -> new ResolvedPolicyImpactAssessment(compiler,
                Map.of(MODE, (input, change) -> null)).assess(context));
        var failure = new IllegalStateException("Owner assessment unavailable");
        assertSame(failure, assertThrows(IllegalStateException.class, () -> new ResolvedPolicyImpactAssessment(compiler,
                Map.of(MODE, (input, change) -> { throw failure; })).assess(context)));
    }

    @Test
    void an_unchanged_policy_cannot_be_constructed_as_a_change() {
        assertThrows(IllegalArgumentException.class,
                () -> new ResolvedPolicyChange(MODE, value("new", "assisted"), value("new", "assisted")));
        assertThrows(IllegalArgumentException.class,
                () -> new ResolvedPolicyChange(MODE, Optional.empty(), Optional.empty()));
    }

    private static ConfigurationImpactContribution effect(String text) {
        return new ConfigurationImpactContribution(List.of(text), List.of(
                new ConfigurationImpactFinding(ConfigurationImpactClassification.CONSEQUENTIAL, text)));
    }

    private static Optional<ResolvedPolicyChange.EffectiveValue> value(String release, String value) {
        return Optional.of(new ResolvedPolicyChange.EffectiveValue(release, value));
    }

    private static CapabilityConfigurationDecisionIdentity identity(String capability, String policy) {
        return new CapabilityConfigurationDecisionIdentity(capability, policy);
    }

    private static MerchantConfiguration base(Set<String> selected, Set<PolicySelection> policies) {
        return new MerchantConfiguration("merchant-a", "c1", 1, "old", selected, policies, Optional.empty(), Optional.empty());
    }

    private static ConfigurationImpactContext context(ConfigurationCompiler compiler, Optional<MerchantConfiguration> base,
                                                      Set<String> selected, Set<PolicySelection> policies) {
        var candidate = new MerchantConfiguration("merchant-a", "c2", base.isPresent() ? 2 : 1, "new", selected, policies,
                base.map(MerchantConfiguration::configurationIdentifier), Optional.empty());
        var resolved = new ConfigurationPackageResolver(compiler).resolve(candidate, "compiler", AT);
        var validation = new ConfigurationValidationEvidence("v1", "merchant-a", "c2", "new", "p1",
                ConfigurationValidationOutcome.SUCCEEDED, "compiler", AT, AT);
        return new ConfigurationImpactContext(candidate, base, validation, resolved);
    }

    private static ConfigurationCompiler compiler(String oldDefault, String newDefault) {
        var registry = new InMemorySemanticRegistry();
        registry.publish(snapshot("old", oldDefault));
        registry.publish(snapshot("new", newDefault));
        return new ConfigurationCompiler(registry);
    }

    private static SemanticRegistrySnapshot snapshot(String release, String defaultValue) {
        var policies = List.of(new OwnedPolicyDefinition("mode", defaultValue, Set.of("assisted", "automatic")));
        return new SemanticRegistrySnapshot(release, Set.of(
                new RegisteredCapability("booking", List.of(), List.of(), policies),
                new RegisteredCapability("enquiry", List.of(), List.of(), policies)));
    }
}
