package mainstreet.semantic.configuration;

import mainstreet.application.MerchantScope;
import mainstreet.fulfilment.FulfilmentBindingSetRevisionReference;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Authority: MS-PROT-040 v1.0,
 * designs/MS-PROT-040 — Merchant Configuration Review, Approval, Activation & Change Model.md,
 * §13 — Change set; §14 — Change-set provenance; §19 — Candidate generation;
 * §20 — Configuration diff; §44 — Base-revision concurrency invariant.
 */
class ConfigurationChangeSetTest {
    private static final PolicySelection OLD = new PolicySelection("booking", "mode", "assisted");
    private static final PolicySelection NEW = new PolicySelection("booking", "mode", "self-service");
    private static final Instant PROPOSED_AT = Instant.parse("2026-09-06T00:00:00Z");

    @Test
    void constructs_complete_candidate_and_retains_exact_base_and_supplied_release() {
        var base = base("merchant-a", "c1");
        var change = change(Set.of("booking", "payment"), Set.of(NEW));
        var candidate = change.candidateFrom(base, "c2", 2, "registry-b");

        assertEquals("merchant-a", candidate.merchantIdentifier());
        assertEquals("c2", candidate.configurationIdentifier());
        assertEquals(Optional.of("c1"), candidate.baseConfigurationIdentifier());
        assertEquals("registry-b", candidate.semanticRegistryVersion());
        assertEquals(Set.of("booking", "payment"), candidate.capabilityIdentifiers());
        assertEquals(Set.of(NEW), candidate.policySelections());
        assertEquals(Optional.empty(), candidate.fulfilmentBindingSetRevisionReference());
        assertEquals(Set.of("booking", "delivery"), base.capabilityIdentifiers());
        assertEquals(Set.of(OLD), base.policySelections());
        assertEquals(candidate, change.candidateFrom(base, "c2", 2, "registry-b"));
    }

    @Test
    void rejects_foreign_or_different_base_instead_of_silently_rebasing() {
        var change = change(Set.of("booking"), Set.of());
        for (var wrongBase : Set.of(base("merchant-b", "c1"), base("merchant-a", "c9"))) {
            assertThrows(IllegalArgumentException.class,
                    () -> change.candidateFrom(wrongBase, "c2", 2, "registry-a"));
            assertThrows(IllegalArgumentException.class, () -> change.differenceFrom(wrongBase));
        }
        assertThrows(IllegalArgumentException.class,
                () -> change.candidateFrom(base("merchant-a", "c1"), "c1", 2, "registry-a"));
    }

    @Test
    void diff_preserves_additions_removals_policy_replacement_and_binding_removal() {
        var base = base("merchant-a", "c1");
        var difference = change(Set.of("booking", "payment"), Set.of(NEW)).differenceFrom(base);
        assertEquals(Set.of("payment"), difference.enabledCapabilities());
        assertEquals(Set.of("delivery"), difference.disabledCapabilities());
        assertEquals(Set.of(NEW), difference.selectedPolicies());
        assertEquals(Set.of(OLD), difference.deselectedPolicies());
        assertEquals(base.fulfilmentBindingSetRevisionReference(), difference.previousBinding());
        assertEquals(Optional.empty(), difference.proposedBinding());
    }

    @Test
    void intent_and_diff_cannot_be_mutated_through_input_or_returned_collections() {
        var capabilities = new HashSet<>(Set.of("booking"));
        var policies = new HashSet<>(Set.of(NEW));
        var change = change(capabilities, policies);
        capabilities.add("payment");
        policies.clear();
        assertEquals(Set.of("booking"), change.capabilityIdentifiers());
        assertEquals(Set.of(NEW), change.policySelections());
        assertThrows(UnsupportedOperationException.class, () -> change.capabilityIdentifiers().clear());
        assertThrows(UnsupportedOperationException.class, () -> change.policySelections().clear());
        var diff = change.differenceFrom(base("merchant-a", "c1"));
        assertThrows(UnsupportedOperationException.class, () -> diff.disabledCapabilities().clear());
        assertThrows(UnsupportedOperationException.class, () -> diff.selectedPolicies().clear());
    }

    @Test
    void retains_each_origin_and_requires_source_attribution() {
        for (var origin : ConfigurationChangeSet.Origin.values()) {
            var provenance = new ConfigurationChangeSet.Provenance(origin, "source-1", "principal-1", PROPOSED_AT);
            var change = new ConfigurationChangeSet(new MerchantScope("merchant-a"), "change-1", "c1",
                    Set.of("booking"), Set.of(), Optional.empty(), provenance);
            assertEquals(provenance, change.provenance());
            assertEquals(origin, change.provenance().origin());
            assertEquals(PROPOSED_AT, change.provenance().proposedAt());
        }
        assertThrows(IllegalArgumentException.class, () -> new ConfigurationChangeSet.Provenance(
                ConfigurationChangeSet.Origin.INFERENCE_PROPOSED, " ", "principal-1", PROPOSED_AT));
        assertThrows(IllegalArgumentException.class, () -> new ConfigurationChangeSet.Provenance(
                ConfigurationChangeSet.Origin.MERCHANT_INITIATED, "source-1", " ", PROPOSED_AT));
    }

    @Test
    void candidate_uses_existing_configuration_shape_checks() {
        var change = change(Set.of("booking"), Set.of(OLD, NEW));
        assertThrows(IllegalArgumentException.class,
                () -> change.candidateFrom(base("merchant-a", "c1"), "c2", 2, "registry-a"));
        assertThrows(IllegalArgumentException.class,
                () -> change(Set.of("booking"), Set.of()).candidateFrom(base("merchant-a", "c1"), "c2", 0, "registry-a"));
    }

    @Test
    void retains_binding_replacement_and_does_not_invent_other_differences() {
        var base = base("merchant-a", "c1");
        var binding = Optional.of(new FulfilmentBindingSetRevisionReference("bindings", 2));
        var change = new ConfigurationChangeSet(new MerchantScope("merchant-a"), "change-2", "c1",
                base.capabilityIdentifiers(), base.policySelections(), binding,
                change(Set.of(), Set.of()).provenance());
        var diff = change.differenceFrom(base);
        assertTrue(diff.enabledCapabilities().isEmpty());
        assertTrue(diff.disabledCapabilities().isEmpty());
        assertTrue(diff.selectedPolicies().isEmpty());
        assertTrue(diff.deselectedPolicies().isEmpty());
        assertEquals(binding, diff.proposedBinding());
        assertEquals(base.fulfilmentBindingSetRevisionReference(), diff.previousBinding());
        assertEquals(binding, change.candidateFrom(base, "c2", 2, "registry-a")
                .fulfilmentBindingSetRevisionReference());
    }

    @Test
    void rejects_missing_change_identity_base_and_provenance() {
        var provenance = change(Set.of(), Set.of()).provenance();
        assertThrows(IllegalArgumentException.class, () -> new ConfigurationChangeSet(
                new MerchantScope("merchant-a"), " ", "c1", Set.of(), Set.of(), Optional.empty(), provenance));
        assertThrows(IllegalArgumentException.class, () -> new ConfigurationChangeSet(
                new MerchantScope("merchant-a"), "change-1", " ", Set.of(), Set.of(), Optional.empty(), provenance));
        assertThrows(NullPointerException.class, () -> new ConfigurationChangeSet(
                new MerchantScope("merchant-a"), "change-1", "c1", Set.of(), Set.of(), Optional.empty(), null));
    }

    private static ConfigurationChangeSet change(Set<String> capabilities, Set<PolicySelection> policies) {
        return new ConfigurationChangeSet(new MerchantScope("merchant-a"), "change-1", "c1",
                capabilities, policies, Optional.empty(), new ConfigurationChangeSet.Provenance(
                ConfigurationChangeSet.Origin.MERCHANT_INITIATED, "source-1", "principal-1", PROPOSED_AT));
    }

    private static MerchantConfiguration base(String merchant, String id) {
        return new MerchantConfiguration(merchant, id, 1, "registry-a", Set.of("booking", "delivery"),
                Set.of(OLD), Optional.empty(), Optional.of(new FulfilmentBindingSetRevisionReference("bindings", 1)));
    }
}
