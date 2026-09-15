package mainstreet.semantic.event;

import java.util.Objects;

/** Exact original event meaning; later registry releases must not silently reinterpret it. */
public record EventContractAffinity(EventContractIdentity contractIdentity, String semanticRegistryReleaseIdentifier) {
    public EventContractAffinity {
        Objects.requireNonNull(contractIdentity, "contractIdentity");
        EventContractIdentity.requireReference(semanticRegistryReleaseIdentifier, "semantic release");
    }
}
