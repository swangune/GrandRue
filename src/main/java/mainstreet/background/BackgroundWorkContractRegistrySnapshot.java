package mainstreet.background;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/** Exact semantic-release registry of declared responsibilities. Empty is valid and grants no execution. */
public final class BackgroundWorkContractRegistrySnapshot {
    private final String release;
    private final Map<BackgroundWorkContractIdentity, BackgroundWorkContractDefinition> contracts;

    public BackgroundWorkContractRegistrySnapshot(
            String semanticRegistryReleaseIdentifier,
            Collection<BackgroundWorkContractDefinition> definitions
    ) {
        BackgroundWorkContractIdentity.requireReference(
                semanticRegistryReleaseIdentifier,
                "semantic release"
        );
        this.release = semanticRegistryReleaseIdentifier;

        var index =
                new HashMap<
                        BackgroundWorkContractIdentity,
                        BackgroundWorkContractDefinition
                >();

        for (var definition : Objects.requireNonNull(definitions, "definitions")) {
            Objects.requireNonNull(definition, "definition");

            if (index.putIfAbsent(definition.identity(), definition) != null) {
                throw new IllegalArgumentException(
                        "Duplicate BackgroundWorkContract identity"
                );
            }
        }

        contracts = Map.copyOf(index);
    }

    public String semanticRegistryReleaseIdentifier() {
        return release;
    }

    public Set<BackgroundWorkContractDefinition> contracts() {
        return Set.copyOf(contracts.values());
    }

    public Optional<BackgroundWorkContractDefinition> contract(
            String requestedRelease,
            BackgroundWorkContractIdentity identity
    ) {
        BackgroundWorkContractIdentity.requireReference(
                requestedRelease,
                "requested semantic release"
        );
        Objects.requireNonNull(identity, "identity");

        return release.equals(requestedRelease)
                ? Optional.ofNullable(contracts.get(identity))
                : Optional.empty();
    }
}