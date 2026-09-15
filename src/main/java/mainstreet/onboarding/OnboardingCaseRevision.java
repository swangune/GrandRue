package mainstreet.onboarding;

/**
 * Exact logical revision identity for one Onboarding Case evidence state.
 *
 * <p>The value is intentionally opaque. Ordering, numbering and persistence strategy are not
 * implied by this type.</p>
 */
public record OnboardingCaseRevision(String value) {

    public OnboardingCaseRevision {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Onboarding case revision must not be blank");
        }
    }
}
