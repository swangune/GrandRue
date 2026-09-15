package mainstreet.onboarding;

/**
 * MS-PROT-052 governing priority classes for adaptive onboarding sequencing.
 *
 * <p>Declaration order is significant: higher-information discovery precedes
 * mandatory configuration, required data, optional integrations and optional
 * profile enrichment.</p>
 */
public enum OnboardingPromptPriority {
    HIGH_BRANCH_DISCOVERY,
    MANDATORY_CONFIGURATION_DECISION,
    REQUIRED_STRUCTURED_DATA,
    OPTIONAL_INTEGRATION,
    OPTIONAL_PROFILE_ENRICHMENT
}
