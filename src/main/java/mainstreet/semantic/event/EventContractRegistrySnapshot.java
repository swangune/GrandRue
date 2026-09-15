package mainstreet.semantic.event;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/** Immutable registry for one exact semantic release. No latest-release or same-name owner fallback. */
public final class EventContractRegistrySnapshot {
    private final String release;
    private final Map<EventContractIdentity, EventContractDefinition> contracts;

    public EventContractRegistrySnapshot(String semanticRegistryReleaseIdentifier,
            Collection<EventContractDefinition> definitions) {
        EventContractIdentity.requireReference(semanticRegistryReleaseIdentifier, "semantic release");
        release = semanticRegistryReleaseIdentifier;
        var index = new HashMap<EventContractIdentity, EventContractDefinition>();
        for (var definition : Objects.requireNonNull(definitions, "definitions")) {
            Objects.requireNonNull(definition, "definition");
            if (index.putIfAbsent(definition.identity(), definition) != null) {
                throw new IllegalArgumentException("Duplicate Event Contract identity");
            }
        }
        contracts = Map.copyOf(index);
    }

    public String semanticRegistryReleaseIdentifier() {
        return release;
    }

    public Set<EventContractDefinition> contracts() {
        return Set.copyOf(contracts.values());
    }

    public Optional<EventContractDefinition> contract(EventContractAffinity affinity) {
        Objects.requireNonNull(affinity, "affinity");
        return release.equals(affinity.semanticRegistryReleaseIdentifier())
                ? Optional.ofNullable(contracts.get(affinity.contractIdentity()))
                : Optional.empty();
    }
}
