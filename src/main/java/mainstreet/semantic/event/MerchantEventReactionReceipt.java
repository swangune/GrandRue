package mainstreet.semantic.event;

import mainstreet.application.MerchantScope;
import java.time.Instant;
import java.util.Objects;

/**
 * Pins responsibility across physical redelivery without rebinding its historical meaning.
 * Authority: MS-PROT-026 v1.1,
 * designs/MS-PROT-026 v1.1 — Production Domain Event Publication, Reaction & Consumption Contract Amendment.md,
 * §23 — Reaction Deduplication; §32 — Historical Affinity.
 */
public record MerchantEventReactionReceipt(
        EventReactionIdentity identity,
        EventReactionContractAffinity contractAffinity,
        EventContractAffinity sourceEventContract,
        MerchantScope merchantScope,
        String downstreamLogicalIntentReference,
        Instant acceptedAt) {
    public MerchantEventReactionReceipt {
        Objects.requireNonNull(identity, "identity");
        Objects.requireNonNull(contractAffinity, "contractAffinity");
        Objects.requireNonNull(sourceEventContract, "sourceEventContract");
        Objects.requireNonNull(merchantScope, "merchantScope");
        EventContractIdentity.requireReference(downstreamLogicalIntentReference, "downstreamLogicalIntentReference");
        Objects.requireNonNull(acceptedAt, "acceptedAt");
        if (!identity.contractIdentity().equals(contractAffinity.contractIdentity())) {
            throw new IllegalArgumentException("Receipt identity must match reaction affinity");
        }
    }

    public boolean sameResponsibility(MerchantEventReactionReceipt other) {
        return other != null && identity.equals(other.identity)
                && contractAffinity.equals(other.contractAffinity)
                && sourceEventContract.equals(other.sourceEventContract)
                && merchantScope.equals(other.merchantScope)
                && downstreamLogicalIntentReference.equals(other.downstreamLogicalIntentReference);
    }
}
