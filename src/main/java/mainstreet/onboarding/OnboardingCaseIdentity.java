package mainstreet.onboarding;

/** Stable identity of one Onboarding Case. */
public record OnboardingCaseIdentity(String value) {

    public OnboardingCaseIdentity {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Onboarding Case identity must not be blank");
        }
    }
}
