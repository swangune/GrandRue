package mainstreet.onboarding;

/**
 * Stable onboarding question identity independent of merchant-facing wording.
 *
 * @param namespace identity namespace
 * @param identifier identity-local question identifier
 */
public record OnboardingQuestionIdentity(String namespace, String identifier) {

    public OnboardingQuestionIdentity {
        namespace = requireNonBlank(namespace, "namespace");
        identifier = requireNonBlank(identifier, "identifier");
    }

    private static String requireNonBlank(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
        return value;
    }
}
