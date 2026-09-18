package grandrue.onboarding;

import grandrue.onboarding.OnboardingPersistenceFailureCategory;
import java.util.Objects;

/**
 * Recoverable Onboarding Case persistence failure.
 */
public final class OnboardingCasePersistenceException
        extends IllegalStateException {

    private final OnboardingPersistenceFailureCategory category;

    public OnboardingCasePersistenceException(
            OnboardingPersistenceFailureCategory category,
            String message
    ) {
        super(message);
        this.category = Objects.requireNonNull(category, "category");
    }

    public OnboardingPersistenceFailureCategory category() {
        return category;
    }
}
