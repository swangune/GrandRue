package grandrue.onboarding;

/** Stable identity of one immutable Initial Configuration Intent. */
public record InitialConfigurationIntentIdentity(String value) {

    public InitialConfigurationIntentIdentity {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Initial Configuration Intent identity must not be blank"
            );
        }
    }
}
