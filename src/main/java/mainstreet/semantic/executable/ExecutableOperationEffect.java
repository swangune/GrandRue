package mainstreet.semantic.executable;

/** One resolved declarative authoritative effect in an executable operation. */
public sealed interface ExecutableOperationEffect
        permits ExecutableObjectCreationEffect,
        ExecutableStateTransitionEffect,
        ExecutableDataMutationEffect,
        ExecutableRelationshipEstablishmentEffect,
        ExecutableRelationshipRemovalEffect,
        ExecutableAllocationClaimEffect,
        ExecutableAllocationReleaseEffect {
}
