package mainstreet.onboarding;

/**
 * MS-PROT-052 authority/purpose classification for onboarding interactions.
 *
 * <p>The class follows what the interaction is authoritative for, not the
 * visual control used to present it.</p>
 */
public enum OnboardingPromptClass {
    DISCOVERY_QUESTION,
    CONFIGURATION_DECISION_QUESTION,
    DATA_CAPTURE_PROMPT,
    REVIEW_CONFIRMATION
}
