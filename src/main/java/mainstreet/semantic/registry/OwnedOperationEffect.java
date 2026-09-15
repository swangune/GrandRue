package mainstreet.semantic.registry;

/**
 * Platform-owned description of one bounded authoritative effect belonging to
 * a registered operation.
 */
public sealed interface OwnedOperationEffect
        permits OwnedObjectCreationEffect,
        OwnedStateTransitionEffect,
        OwnedDataMutationEffect,
        OwnedRelationshipEstablishmentEffect,
        OwnedRelationshipRemovalEffect,
        OwnedAllocationClaimEffect,
        OwnedAllocationReleaseEffect {
}
