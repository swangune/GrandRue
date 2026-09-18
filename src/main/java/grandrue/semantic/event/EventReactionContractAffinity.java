package grandrue.semantic.event;

import java.util.Objects;

/** Historical reaction contract meaning; release provenance is not a fresh business intention. */
public record EventReactionContractAffinity(EventReactionContractIdentity contractIdentity,
                                           String semanticRegistryReleaseIdentifier) {
    public EventReactionContractAffinity {
        Objects.requireNonNull(contractIdentity, "contractIdentity");
        EventContractIdentity.requireReference(semanticRegistryReleaseIdentifier, "semantic release");
    }
}
