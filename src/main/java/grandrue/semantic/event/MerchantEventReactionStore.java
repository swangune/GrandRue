package grandrue.semantic.event;

import java.util.List;
import java.util.Optional;

/**
 * Records supplied progression evidence; callers must establish source and execution authority.
 * An acknowledgement belongs to one consumer responsibility, not to event publication.
 * Authority: MS-PROT-026 v1.1 — Production Domain Event Publication, Reaction & Consumption Contract Amendment,
 * §17 — Per-Reaction Acknowledgement; §24 — Downstream Idempotency Remains Separate.
 */
public interface MerchantEventReactionStore {
    MerchantEventReactionReceipt accept(MerchantEventReactionReceipt candidate);
    Optional<MerchantEventReactionReceipt> receipt(EventReactionIdentity identity);
    List<MerchantEventReactionReceipt> pending(EventReactionContractAffinity affinity, int limit);
    EventReactionAcknowledgement acknowledge(EventReactionAcknowledgement candidate);
    Optional<EventReactionAcknowledgement> acknowledgement(EventReactionIdentity identity);
}
