package grandrue.observability;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Exact semantic-release registry of operational evidence responsibilities.
 * Registration describes diagnosability only; it never authorises mutation.
 */
public final class OperationalEvidenceContractRegistrySnapshot {

    private final String release;
    private final Map<
            OperationalEvidenceContractIdentity,
            OperationalEvidenceContractDefinition
            > contracts;

    public OperationalEvidenceContractRegistrySnapshot(
            String semanticRegistryReleaseIdentifier,
            Collection<OperationalEvidenceContractDefinition> definitions
    ) {
        OperationalEvidenceContractIdentity.requireReference(
                semanticRegistryReleaseIdentifier,
                "semanticRegistryReleaseIdentifier"
        );
        release = semanticRegistryReleaseIdentifier;

        Map<
                OperationalEvidenceContractIdentity,
                OperationalEvidenceContractDefinition
                > index = new HashMap<>();

        for (OperationalEvidenceContractDefinition definition :
                Objects.requireNonNull(definitions, "definitions")) {
            Objects.requireNonNull(definition, "definition");
            if (index.putIfAbsent(
                    definition.identity(),
                    definition
            ) != null) {
                throw new IllegalArgumentException(
                        "Duplicate OperationalEvidenceContract identity"
                );
            }
        }

        contracts = Map.copyOf(index);
    }

    public String semanticRegistryReleaseIdentifier() {
        return release;
    }

    public Set<OperationalEvidenceContractDefinition> contracts() {
        return Set.copyOf(contracts.values());
    }

    public Optional<OperationalEvidenceContractDefinition> contract(
            String requestedRelease,
            OperationalEvidenceContractIdentity identity
    ) {
        OperationalEvidenceContractIdentity.requireReference(
                requestedRelease,
                "requestedRelease"
        );
        Objects.requireNonNull(identity, "identity");

        if (!release.equals(requestedRelease)) {
            return Optional.empty();
        }
        return Optional.ofNullable(contracts.get(identity));
    }
}
