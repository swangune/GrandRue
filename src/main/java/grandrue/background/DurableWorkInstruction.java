package grandrue.background;

import mainstreet.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Infrastructure instruction that a future responsibility must be attempted or
 * re-evaluated. It is not authority for the referenced business mutation.
 */
public record DurableWorkInstruction(
        String workIdentity,
        String ownerContextIdentifier,
        BackgroundExecutionScope executionScope,
        Optional<MerchantScope> merchantScope,
        Instant dueAt,
        String responsibilityIdentifier,
        String correlationIdentifier,
        Optional<String> causationIdentifier,
        Optional<String> semanticProvenanceReference,
        String retryPolicyReference,
        OverdueHandling overdueHandling,
        Instant createdAt,
        Optional<BackgroundWorkContractAffinity> contractAffinity
) {
    public DurableWorkInstruction {
        require(workIdentity, "workIdentity");
        require(ownerContextIdentifier, "ownerContextIdentifier");
        Objects.requireNonNull(executionScope, "executionScope");
        merchantScope = Objects.requireNonNull(merchantScope, "merchantScope");
        Objects.requireNonNull(dueAt, "dueAt");
        require(responsibilityIdentifier, "responsibilityIdentifier");
        require(correlationIdentifier, "correlationIdentifier");
        causationIdentifier = normalise(causationIdentifier, "causationIdentifier");
        semanticProvenanceReference = normalise(
                semanticProvenanceReference,
                "semanticProvenanceReference"
        );
        require(retryPolicyReference, "retryPolicyReference");
        Objects.requireNonNull(overdueHandling, "overdueHandling");
        Objects.requireNonNull(createdAt, "createdAt");
        contractAffinity = Objects.requireNonNull(contractAffinity, "contractAffinity");
        contractAffinity.ifPresent(affinity -> {
            if (!ownerContextIdentifier.equals(affinity.contractIdentity().ownerIdentifier())) {
                throw new IllegalArgumentException("Contract owner must match instruction owner");
            }
        });

        if (executionScope == BackgroundExecutionScope.MERCHANT && merchantScope.isEmpty()) {
            throw new IllegalArgumentException(
                    "MERCHANT background execution scope requires Merchant Scope"
            );
        }
        if (executionScope == BackgroundExecutionScope.PLATFORM && merchantScope.isPresent()) {
            throw new IllegalArgumentException(
                    "PLATFORM background execution scope must not fabricate Merchant Scope"
            );
        }
    }

    /** Compatibility for unregistered technical instructions; never infers a semantic release. */
    public DurableWorkInstruction(String workIdentity, String ownerContextIdentifier,
            BackgroundExecutionScope executionScope, Optional<MerchantScope> merchantScope,
            Instant dueAt, String responsibilityIdentifier, String correlationIdentifier,
            Optional<String> causationIdentifier, Optional<String> semanticProvenanceReference,
            String retryPolicyReference, OverdueHandling overdueHandling, Instant createdAt) {
        this(workIdentity, ownerContextIdentifier, executionScope, merchantScope, dueAt,
                responsibilityIdentifier, correlationIdentifier, causationIdentifier,
                semanticProvenanceReference, retryPolicyReference, overdueHandling, createdAt, Optional.empty());
    }

    public DurableWorkInstruction withContractAffinity(BackgroundWorkContractAffinity affinity) {
        return new DurableWorkInstruction(workIdentity, ownerContextIdentifier, executionScope, merchantScope,
                dueAt, responsibilityIdentifier, correlationIdentifier, causationIdentifier,
                semanticProvenanceReference, retryPolicyReference, overdueHandling, createdAt,
                Optional.of(affinity));
    }

    /** Static affinity resolution only. This does not establish due conditions, trusted scope or mutation authority. */
    public Optional<BackgroundWorkContractDefinition> registeredContract(BackgroundWorkContractRegistrySnapshot registry) {
        Objects.requireNonNull(registry, "registry");
        return contractAffinity.flatMap(affinity ->
                registry.contract(affinity.semanticRegistryReleaseIdentifier(), affinity.contractIdentity()))
                .filter(contract -> contract.authorityScope() == executionScope)
                .filter(contract -> contract.target().targetIdentifier().equals(responsibilityIdentifier))
                .filter(contract -> contract.retryContractReference().equals(retryPolicyReference))
                .filter(contract -> contract.overdueHandling() == overdueHandling);
    }

    private static Optional<String> normalise(Optional<String> value, String label) {
        Objects.requireNonNull(value, label);
        value.ifPresent(item -> require(item, label));
        return value;
    }

    private static void require(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
