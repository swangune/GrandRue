package mainstreet.api;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/** Immutable registry snapshot of explicitly registered production API contracts. */
public record ApiContractRegistrySnapshot(
        Set<ApiContractRegistration> contracts
) {
    public ApiContractRegistrySnapshot {
        contracts = Set.copyOf(Objects.requireNonNull(contracts, "contracts"));
        requireUniqueIdentities(contracts);
    }

    public Optional<ApiContractRegistration> contract(ApiContractIdentity identity) {
        Objects.requireNonNull(identity, "identity");
        return contracts.stream()
                .filter(candidate -> candidate.identity().equals(identity))
                .findFirst();
    }

    private static void requireUniqueIdentities(
            Set<ApiContractRegistration> contracts
    ) {
        Set<ApiContractIdentity> identities = new HashSet<>();
        for (ApiContractRegistration contract : contracts) {
            if (!identities.add(contract.identity())) {
                throw new IllegalArgumentException(
                        "Duplicate API contract identity: " + contract.identity()
                );
            }
        }
    }
}
