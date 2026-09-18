package grandrue.onboarding;

/**
 * Exact material version identity for an onboarding question definition.
 *
 * <p>The value is intentionally opaque. It preserves historical interpretation affinity without
 * implying numeric ordering, contiguous sequencing or compatibility between versions.</p>
 */
public record OnboardingQuestionDefinitionVersion(String value) {

    public OnboardingQuestionDefinitionVersion {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Onboarding question definition version must not be blank");
        }
    }
}
