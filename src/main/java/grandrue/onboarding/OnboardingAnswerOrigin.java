package grandrue.onboarding;

/**
 * MS-PROT-052 provenance describing how onboarding evidence or selection
 * entered a configuration proposal.
 *
 * <p>This taxonomy is onboarding evidence provenance only. It does not confer
 * configuration or runtime authority.</p>
 */
public enum OnboardingAnswerOrigin {
    MERCHANT_SELECTED,
    MERCHANT_APPROVED_IN_REVIEW,
    INFERRED_PROPOSAL,
    DERIVED,
    DEFAULTED,
    IMPORTED_EVIDENCE
}
