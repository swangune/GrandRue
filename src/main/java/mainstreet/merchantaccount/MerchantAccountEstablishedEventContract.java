package mainstreet.merchantaccount;

import mainstreet.semantic.event.*;
import java.util.List;
import java.util.Set;

/** Initial immutable mapping of MS-PROT-071 §21 and v1.2 §§3–6 under MS-PROT-026 v1.1. */
public final class MerchantAccountEstablishedEventContract {
    public static final EventContractIdentity IDENTITY =
            new EventContractIdentity("merchant-account", "MerchantAccountEstablished");
    public static final EventContractAffinity AFFINITY =
            new EventContractAffinity(IDENTITY, "merchant-account-event-contracts@1");
    public static final EventContractDefinition DEFINITION = new EventContractDefinition(
            IDENTITY,
            "MS-PROT-071/21:committed-merchant-account-existence",
            "MS-PROT-071/21:merchant-scope",
            "MS-PROT-071/21:merchantIdentifier",
            Set.of("MS-PROT-071-v1.2/5:establishmentIdentity",
                    "MS-PROT-071-v1.2/5:logicalEstablishmentRequestIdentity",
                    "MS-PROT-071-v1.2/5:occurredAt-equals-establishedAt"),
            "MS-PROT-071-v1.2/5:MerchantAccountEstablished",
            "MS-PROT-026-v1.1/5:preserve-exact-original-meaning");

    private MerchantAccountEstablishedEventContract() { }

    public static EventContractRegistrySnapshot registry() {
        return new EventContractRegistrySnapshot(AFFINITY.semanticRegistryReleaseIdentifier(), List.of(DEFINITION));
    }
}
