package grandrue.semantic.event;

import grandrue.application.StandingFreeEventReactionContract;
import grandrue.merchantaccount.MerchantAccountEstablishedEventContract;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class EventReactionContractTest {
    private static final EventContractAffinity SOURCE = MerchantAccountEstablishedEventContract.AFFINITY;
    private static EventReactionContractDefinition definition(String owner, String id) {
        return new EventReactionContractDefinition(new EventReactionContractIdentity(owner, id), SOURCE,
                "purpose", "scope", "mode", new EventReactionTargetReference(owner, "operation"),
                "principal", "duplicate-rule", "ordering", "supersession", Set.of("required-data"),
                "failure-retry", "downstream-intent");
    }
    @Test void duplicate_delivery_keeps_one_identity_while_other_reactions_and_events_are_independent() {
        var contract = new EventReactionContractIdentity("commercial", "standing-free");
        assertEquals(new EventReactionIdentity("event-1", contract), new EventReactionIdentity("event-1", contract));
        assertNotEquals(new EventReactionIdentity("event-1", contract), new EventReactionIdentity("event-2", contract));
        assertNotEquals(new EventReactionIdentity("event-1", contract),
                new EventReactionIdentity("event-1", new EventReactionContractIdentity("projection", "standing-free")));
    }
    @Test void registry_release_is_affinity_not_a_new_logical_reaction() {
        var id = new EventReactionContractIdentity("owner", "reaction");
        var first = new EventReactionContractAffinity(id, "release-1");
        var second = new EventReactionContractAffinity(id, "release-2");
        assertNotEquals(first, second);
        assertEquals(new EventReactionIdentity("event", first.contractIdentity()),
                new EventReactionIdentity("event", second.contractIdentity()));
    }
    @Test void mandatory_contract_semantics_cannot_be_missing_or_blank() {
        var d = definition("owner", "reaction");
        for (int field = 0; field < 9; field++) {
            var refs = new String[]{"purpose", "scope", "mode", "principal", "duplicate", "ordering", "supersession", "retry", "intent"};
            refs[field] = " ";
            assertThrows(IllegalArgumentException.class, () -> new EventReactionContractDefinition(
                    d.identity(), SOURCE, refs[0], refs[1], refs[2], d.target(), refs[3], refs[4], refs[5],
                    refs[6], Set.of("data"), refs[7], refs[8]));
        }
        assertThrows(IllegalArgumentException.class, () -> new EventReactionContractDefinition(
                d.identity(), SOURCE, "purpose", "scope", "mode", d.target(), "principal", "duplicate",
                "ordering", "supersession", Set.of(), "retry", "intent"));
        assertThrows(IllegalArgumentException.class, () -> new EventReactionIdentity(" ", d.identity()));
        assertThrows(IllegalArgumentException.class, () -> new EventReactionContractIdentity("", "reaction"));
        assertThrows(IllegalArgumentException.class, () -> new EventReactionContractAffinity(d.identity(), " "));
        assertThrows(IllegalArgumentException.class, () -> new EventReactionTargetReference("owner", " "));
    }
    @Test void lookup_requires_exact_reaction_release_and_exact_source_contract_affinity() {
        var d = definition("owner", "reaction");
        var registry = new EventReactionContractRegistrySnapshot("r1", List.of(d));
        var affinity = new EventReactionContractAffinity(d.identity(), "r1");
        assertEquals(Optional.of(d), registry.contract(affinity, SOURCE));
        assertTrue(registry.contract(new EventReactionContractAffinity(d.identity(), "r2"), SOURCE).isEmpty());
        assertTrue(registry.contract(affinity, new EventContractAffinity(SOURCE.contractIdentity(), "other-release")).isEmpty());
        assertTrue(registry.contract(affinity, new EventContractAffinity(new EventContractIdentity("other", "fact"),
                SOURCE.semanticRegistryReleaseIdentifier())).isEmpty());
        assertTrue(registry.contract(new EventReactionContractAffinity(
                new EventReactionContractIdentity("other", "reaction"), "r1"), SOURCE).isEmpty());
    }
    @Test void registry_rejects_duplicates_and_defensively_copies_definitions_and_required_data() {
        var data = new HashSet<>(Set.of("data"));
        var d = new EventReactionContractDefinition(new EventReactionContractIdentity("owner", "reaction"), SOURCE,
                "purpose", "scope", "mode", new EventReactionTargetReference("owner", "op"),
                "principal", "duplicate", "ordering", "supersession", data, "retry", "intent");
        var source = new ArrayList<>(List.of(d));
        var registry = new EventReactionContractRegistrySnapshot("r", source);
        source.clear();
        data.clear();
        assertEquals(Set.of("data"), d.requiredEventDataReferences());
        assertEquals(Set.of(d), registry.contracts());
        assertThrows(UnsupportedOperationException.class, () -> d.requiredEventDataReferences().clear());
        assertThrows(UnsupportedOperationException.class, () -> registry.contracts().clear());
        assertThrows(IllegalArgumentException.class, () -> new EventReactionContractRegistrySnapshot("r", List.of(d, d)));
    }
    @Test void empty_registry_and_null_inputs_do_not_enable_listeners() {
        var registry = new EventReactionContractRegistrySnapshot("r", List.of());
        assertTrue(registry.contract(new EventReactionContractAffinity(
                new EventReactionContractIdentity("owner", "reaction"), "r"), SOURCE).isEmpty());
        assertThrows(NullPointerException.class, () -> registry.contract(null, SOURCE));
        assertThrows(NullPointerException.class, () -> new EventReactionIdentity("event", null));
        assertThrows(IllegalArgumentException.class, () -> new EventReactionContractRegistrySnapshot(" ", List.of()));
    }
    @Test void same_named_reactions_of_different_owners_coexist_without_lookup_collision() {
        var first = definition("commercial", "refresh");
        var second = definition("projection", "refresh");
        var registry = new EventReactionContractRegistrySnapshot("r", List.of(first, second));
        assertEquals(Optional.of(first), registry.contract(new EventReactionContractAffinity(first.identity(), "r"), SOURCE));
        assertEquals(Optional.of(second), registry.contract(new EventReactionContractAffinity(second.identity(), "r"), SOURCE));
        assertEquals(Set.of(first, second), registry.contracts());
    }
    @Test void conflicting_definition_cannot_hide_behind_the_same_registered_identity() {
        var first = definition("owner", "reaction");
        var changed = new EventReactionContractDefinition(first.identity(), SOURCE,
                "different-purpose", first.authorityScopeReference(), first.executionModeReference(),
                first.target(), first.executionPrincipalContractReference(), first.duplicateIdentityRuleReference(),
                first.orderingRequirementReference(), first.supersessionCoalescingRuleReference(),
                first.requiredEventDataReferences(), first.failureRetryContractReference(),
                first.downstreamLogicalIntentRuleReference());
        assertNotEquals(first, changed);
        assertThrows(IllegalArgumentException.class, () ->
                new EventReactionContractRegistrySnapshot("r", List.of(first, changed)));
    }
    @Test void standing_free_registration_preserves_owner_source_temporal_and_downstream_semantics() {
        var d = StandingFreeEventReactionContract.DEFINITION;
        assertEquals("commercial", d.identity().ownerIdentifier());
        assertEquals(SOURCE, d.sourceEventContract());
        assertTrue(d.requiredEventDataReferences().contains("MS-PROT-056-v1.6/3:original-establishment-time"));
        assertTrue(d.downstreamLogicalIntentRuleReference().contains("one-baseline-per-merchant"));
        assertEquals(Optional.of(d), StandingFreeEventReactionContract.registry()
                .contract(StandingFreeEventReactionContract.AFFINITY, SOURCE));
    }
}
