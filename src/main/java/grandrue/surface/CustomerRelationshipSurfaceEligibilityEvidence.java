package grandrue.surface;

import java.util.Objects;

/**
 * Contextual evidence that one registered owner-qualified customer Surface
 * eligibility requirement was satisfied for the current resolution. It records
 * why Surface membership was admitted but grants no relationship or execution
 * authority of its own.
 */
public record CustomerRelationshipSurfaceEligibilityEvidence(
        CustomerSurfaceEligibilityRequirementIdentity requirementIdentity
) implements ContextualSurfaceEligibilityEvidence {
    public CustomerRelationshipSurfaceEligibilityEvidence {
        Objects.requireNonNull(requirementIdentity, "requirementIdentity");
    }
}
