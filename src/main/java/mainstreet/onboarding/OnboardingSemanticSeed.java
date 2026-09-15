package mainstreet.onboarding;

/**
 * Registered semantic intent proposed by onboarding discovery.
 *
 * <p>A seed is candidate input only. It does not activate a capability, create
 * Merchant Configuration or establish runtime authority.</p>
 */
public record OnboardingSemanticSeed(String namespace, String identifier) {

    public OnboardingSemanticSeed {
        namespace = requireNonBlank(namespace, "namespace");
        identifier = requireNonBlank(identifier, "identifier");
    }

    private static String requireNonBlank(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
        return value;
    }
}
