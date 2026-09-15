package mainstreet.semantic.event;

import mainstreet.application.MerchantScope;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;

class MerchantEventReactionReceiptTest {
    private static final Instant T0 = Instant.parse("2026-09-06T00:00:00Z");
    private static final EventReactionContractIdentity CONTRACT = new EventReactionContractIdentity("commercial", "baseline");
    private static MerchantEventReactionReceipt receipt(Instant acceptedAt, String intent) {
        return new MerchantEventReactionReceipt(new EventReactionIdentity("event", CONTRACT),
                new EventReactionContractAffinity(CONTRACT, "r"),
                new EventContractAffinity(new EventContractIdentity("merchant-account", "established"), "s"),
                new MerchantScope("merchant-a"), intent, acceptedAt);
    }

    @Test void physical_acceptance_time_does_not_create_a_new_responsibility() {
        assertTrue(receipt(T0, "intent").sameResponsibility(receipt(T0.plusSeconds(10), "intent")));
        assertFalse(receipt(T0, "intent").sameResponsibility(receipt(T0, "other-intent")));
        assertNotEquals(receipt(T0, "intent"), receipt(T0.plusSeconds(10), "intent"));
    }
    @Test void receipt_rejects_mismatched_contract_key_and_missing_intent() {
        var r = receipt(T0, "intent");
        assertThrows(IllegalArgumentException.class, () -> new MerchantEventReactionReceipt(
                r.identity(), new EventReactionContractAffinity(new EventReactionContractIdentity("other", "baseline"), "r"),
                r.sourceEventContract(), r.merchantScope(), r.downstreamLogicalIntentReference(), T0));
        assertThrows(IllegalArgumentException.class, () -> receipt(T0, " "));
    }
    @Test void acknowledgement_requires_an_exact_reaction_and_outcome_evidence() {
        var id = receipt(T0, "intent").identity();
        assertThrows(IllegalArgumentException.class, () -> new EventReactionAcknowledgement(id, " ", T0));
        assertThrows(NullPointerException.class, () -> new EventReactionAcknowledgement(null, "outcome", T0));
        assertThrows(NullPointerException.class, () -> new EventReactionAcknowledgement(id, "outcome", null));
    }
}
