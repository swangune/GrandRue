package mainstreet.merchantaccount;

import mainstreet.application.MerchantScope;
import mainstreet.infrastructure.persistence.merchantaccount.MerchantAccountEstablishmentPublicationIntent;
import mainstreet.semantic.event.*;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class MerchantAccountEstablishedEventContractTest {
    private static final Instant T0 = Instant.parse("2026-09-06T00:00:00Z");

    private static MerchantAccountEstablishmentPublicationIntent intent(Optional<EventContractAffinity> affinity) {
        return new MerchantAccountEstablishmentPublicationIntent("publication-1", "establishment-1",
                "merchant-1", "request-1", T0, Optional.empty(), affinity);
    }
    @Test void owner_contract_maps_the_accepted_committed_fact_with_distinct_occurrence_identity() {
        var value = intent(Optional.of(MerchantAccountEstablishedEventContract.AFFINITY));
        var occurrence = value.registeredOccurrence(MerchantAccountEstablishedEventContract.registry()).orElseThrow();
        assertEquals("merchant-account-established-event/publication-1", occurrence.eventIdentity());
        assertNotEquals(value.publicationIntentIdentifier(), occurrence.eventIdentity());
        assertNotEquals(value.establishmentIdentity(), occurrence.eventIdentity());
        assertEquals(value.event(), occurrence.fact());
        assertEquals(T0, occurrence.fact().occurredAt());
        assertEquals(MerchantAccountEstablishedEventContract.AFFINITY, occurrence.contractAffinity());
    }
    @Test void legacy_or_unavailable_affinity_cannot_be_reinterpreted_as_current_registration() {
        assertTrue(intent(Optional.empty()).registeredOccurrence(MerchantAccountEstablishedEventContract.registry()).isEmpty());
        var affinity = MerchantAccountEstablishedEventContract.AFFINITY;
        assertTrue(intent(Optional.of(new EventContractAffinity(affinity.contractIdentity(), "different-release")))
                .registeredOccurrence(MerchantAccountEstablishedEventContract.registry()).isEmpty());
        assertTrue(intent(Optional.of(affinity)).registeredOccurrence(
                new EventContractRegistrySnapshot(affinity.semanticRegistryReleaseIdentifier(), List.of())).isEmpty());
    }
    @Test void same_identity_and_release_cannot_bind_a_changed_fact_contract() {
        var original = MerchantAccountEstablishedEventContract.DEFINITION;
        var changed = new EventContractDefinition(original.identity(), "different-fact",
                original.authorityScopeReference(), original.subjectReferenceContractReference(),
                original.requiredProvenanceReferences(), original.payloadContractReference(), original.compatibilityEvolutionReference());
        var registry = new EventContractRegistrySnapshot(
                MerchantAccountEstablishedEventContract.AFFINITY.semanticRegistryReleaseIdentifier(), List.of(changed));
        assertTrue(intent(Optional.of(MerchantAccountEstablishedEventContract.AFFINITY)).registeredOccurrence(registry).isEmpty());
    }
    @Test void malformed_occurrence_and_wrong_contract_are_rejected() {
        var fact = new MerchantAccountEstablished("establishment-1", new MerchantScope("merchant-1"), "request-1", T0);
        assertThrows(IllegalArgumentException.class, () -> new MerchantAccountEstablishedOccurrence(
                " ", MerchantAccountEstablishedEventContract.AFFINITY, fact));
        assertThrows(IllegalArgumentException.class, () -> new MerchantAccountEstablishedOccurrence(
                "event-1", new EventContractAffinity(new EventContractIdentity("other", "fact"), "r"), fact));
        assertThrows(NullPointerException.class, () -> new MerchantAccountEstablishedOccurrence(
                "event-1", MerchantAccountEstablishedEventContract.AFFINITY, null));
    }
}
