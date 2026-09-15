package mainstreet.semantic.execution;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Contract-scoped executable support required by one invocation or deployment
 * admission check. Participant/effect contracts remain explicit so matching
 * an entry operation alone can never establish executable support.
 */
public record ExecutableSupportRequirement(
        SemanticExecutionContractReference affectedExecutionContract,
        Set<SemanticExecutionContractReference> requiredParticipantAndEffectContracts
) {
    public ExecutableSupportRequirement {
        affectedExecutionContract = Objects.requireNonNull(
                affectedExecutionContract,
                "affectedExecutionContract"
        );
        requiredParticipantAndEffectContracts = Set.copyOf(
                Objects.requireNonNull(
                        requiredParticipantAndEffectContracts,
                        "requiredParticipantAndEffectContracts"
                )
        );
    }

    public ExecutableSupportRequirement(
            SemanticExecutionContractReference affectedExecutionContract
    ) {
        this(affectedExecutionContract, Set.of());
    }

    /** Returns every exact contract that the selected path must support. */
    public Set<SemanticExecutionContractReference> requiredContracts() {
        LinkedHashSet<SemanticExecutionContractReference> contracts =
                new LinkedHashSet<>();
        contracts.add(affectedExecutionContract);
        contracts.addAll(requiredParticipantAndEffectContracts);
        return Set.copyOf(contracts);
    }
}
