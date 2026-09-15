package mainstreet.surface;

/**
 * Runtime evidence explaining why an otherwise registered Surface contribution
 * is contextually eligible. Evidence does not grant relationship or execution
 * authority.
 */
public sealed interface ContextualSurfaceEligibilityEvidence
        permits ActorAuthoritySurfaceEligibilityEvidence,
                CustomerRelationshipSurfaceEligibilityEvidence,
                ResidualObligationSurfaceEligibilityEvidence {
}
