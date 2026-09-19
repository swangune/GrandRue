package grandrue.semantic.event;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class EventContractRegistrySnapshotTest {
    private static EventContractDefinition definition(String owner, String id) {
        return new EventContractDefinition(new EventContractIdentity(owner, id),
                "owner/fact-meaning", "owner/authority-scope", "owner/subject-reference",
                Set.of("owner/required-provenance"), "owner/purpose-limited-payload", "owner/evolution");
    }

    @Test void definition_retains_owner_semantics_without_granting_reaction_authority() {
        var contract = definition("ordering", "confirmed");
        assertEquals(new EventContractIdentity("ordering", "confirmed"), contract.identity());
        assertEquals("owner/fact-meaning", contract.factMeaningReference());
        assertEquals("owner/authority-scope", contract.authorityScopeReference());
        assertEquals("owner/subject-reference", contract.subjectReferenceContractReference());
        assertEquals(Set.of("owner/required-provenance"), contract.requiredProvenanceReferences());
        assertEquals("owner/purpose-limited-payload", contract.payloadContractReference());
        assertEquals("owner/evolution", contract.compatibilityEvolutionReference());
    }

    @Test void mandatory_identity_and_semantic_references_cannot_be_missing_or_blank() {
        assertThrows(IllegalArgumentException.class, () -> new EventContractIdentity(" ", "fact"));
        assertThrows(IllegalArgumentException.class, () -> new EventContractIdentity("owner", null));
        var id = new EventContractIdentity("owner", "fact");
        for (int field = 0; field < 5; field++) {
            var refs = new String[]{"meaning", "scope", "subject", "payload", "evolution"};
            refs[field] = " ";
            assertThrows(IllegalArgumentException.class, () -> new EventContractDefinition(
                    id, refs[0], refs[1], refs[2], Set.of("provenance"), refs[3], refs[4]));
        }
        assertThrows(IllegalArgumentException.class, () -> new EventContractDefinition(
                id, "meaning", "scope", "subject", Set.of(), "payload", "evolution"));
        assertThrows(IllegalArgumentException.class, () -> new EventContractDefinition(
                id, "meaning", "scope", "subject", Set.of(" "), "payload", "evolution"));
        assertThrows(NullPointerException.class, () -> new EventContractDefinition(
                null, "meaning", "scope", "subject", Set.of("provenance"), "payload", "evolution"));
    }

    @Test void historical_affinity_never_resolves_to_another_release_or_owner() {
        var first = definition("ordering", "confirmed");
        var second = definition("booking", "confirmed");
        var registry = new EventContractRegistrySnapshot("release-1", List.of(first, second));
        assertEquals(Optional.of(first), registry.contract(new EventContractAffinity(first.identity(), "release-1")));
        assertEquals(Optional.of(second), registry.contract(new EventContractAffinity(second.identity(), "release-1")));
        assertTrue(registry.contract(new EventContractAffinity(first.identity(), "release-2")).isEmpty());
        assertTrue(registry.contract(new EventContractAffinity(new EventContractIdentity("other", "confirmed"), "release-1")).isEmpty());
        assertEquals("release-1", registry.semanticRegistryReleaseIdentifier());
    }

    @Test void duplicate_exact_identity_is_rejected_even_for_equal_definitions() {
        var contract = definition("ordering", "confirmed");
        assertThrows(IllegalArgumentException.class, () ->
                new EventContractRegistrySnapshot("r", List.of(contract, contract)));
    }

    @Test void declarations_and_registry_are_immutable_and_defensively_copied() {
        var provenance = new HashSet<>(Set.of("required"));
        var contract = new EventContractDefinition(new EventContractIdentity("owner", "fact"),
                "meaning", "scope", "subject", provenance, "payload", "evolution");
        var definitions = new ArrayList<>(List.of(contract));
        var registry = new EventContractRegistrySnapshot("r", definitions);
        definitions.clear();
        provenance.clear();
        assertEquals(Set.of("required"), contract.requiredProvenanceReferences());
        assertEquals(Set.of(contract), registry.contracts());
        assertThrows(UnsupportedOperationException.class, () -> contract.requiredProvenanceReferences().clear());
        assertThrows(UnsupportedOperationException.class, () -> registry.contracts().clear());
    }

    @Test void empty_registry_and_malformed_affinity_never_supply_default_meaning() {
        var id = new EventContractIdentity("owner", "fact");
        assertTrue(new EventContractRegistrySnapshot("r", List.of()).contract(new EventContractAffinity(id, "r")).isEmpty());
        assertThrows(IllegalArgumentException.class, () -> new EventContractAffinity(id, " "));
        assertThrows(NullPointerException.class, () -> new EventContractAffinity(null, "r"));
        assertThrows(IllegalArgumentException.class, () -> new EventContractRegistrySnapshot("", List.of()));
        assertThrows(NullPointerException.class, () -> new EventContractRegistrySnapshot("r", List.of()).contract(null));
    }
}
