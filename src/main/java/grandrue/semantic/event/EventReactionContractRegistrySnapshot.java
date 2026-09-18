package grandrue.semantic.event;

import java.util.*;

/** Exact release and source-affinity lookup. No listener discovery or global event acknowledgement. */
public final class EventReactionContractRegistrySnapshot {
    private final String release;
    private final Map<EventReactionContractIdentity, EventReactionContractDefinition> contracts;

    public EventReactionContractRegistrySnapshot(String semanticRegistryReleaseIdentifier,
            Collection<EventReactionContractDefinition> definitions) {
        EventContractIdentity.requireReference(semanticRegistryReleaseIdentifier, "semantic release");
        release = semanticRegistryReleaseIdentifier;
        var index = new HashMap<EventReactionContractIdentity, EventReactionContractDefinition>();
        for (var definition : Objects.requireNonNull(definitions, "definitions")) {
            Objects.requireNonNull(definition, "definition");
            if (index.putIfAbsent(definition.identity(), definition) != null) {
                throw new IllegalArgumentException("Duplicate Event Reaction Contract identity");
            }
        }
        contracts = Map.copyOf(index);
    }

    public String semanticRegistryReleaseIdentifier() { return release; }
    public Set<EventReactionContractDefinition> contracts() { return Set.copyOf(contracts.values()); }

    public Optional<EventReactionContractDefinition> contract(EventReactionContractAffinity affinity,
            EventContractAffinity sourceEventContract) {
        Objects.requireNonNull(affinity, "affinity");
        Objects.requireNonNull(sourceEventContract, "sourceEventContract");
        if (!release.equals(affinity.semanticRegistryReleaseIdentifier())) return Optional.empty();
        return Optional.ofNullable(contracts.get(affinity.contractIdentity()))
                .filter(contract -> contract.sourceEventContract().equals(sourceEventContract));
    }
}
