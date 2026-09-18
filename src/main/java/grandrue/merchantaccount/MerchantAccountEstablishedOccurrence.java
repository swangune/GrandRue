package grandrue.merchantaccount;

import grandrue.semantic.event.EventContractAffinity;
import java.util.Objects;

/** Owner-specific event occurrence; distinct from publication, establishment fact and request identities. */
public record MerchantAccountEstablishedOccurrence(
        String eventIdentity,
        EventContractAffinity contractAffinity,
        MerchantAccountEstablished fact
) {
    public MerchantAccountEstablishedOccurrence {
        if (eventIdentity == null || eventIdentity.isBlank()) {
            throw new IllegalArgumentException("Event identity must not be blank");
        }
        if (!MerchantAccountEstablishedEventContract.AFFINITY.equals(contractAffinity)) {
            throw new IllegalArgumentException("Unsupported MerchantAccountEstablished contract affinity");
        }
        Objects.requireNonNull(fact, "fact");
    }
}
