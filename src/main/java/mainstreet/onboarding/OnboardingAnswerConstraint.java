package mainstreet.onboarding;

import java.util.Set;

/**
 * Registered prompt-local consistency rule for a structured option selection.
 */
@FunctionalInterface
public interface OnboardingAnswerConstraint {

    OnboardingAnswerConstraint UNRESTRICTED = options -> true;

    boolean isSatisfiedBy(Set<String> answerOptionIdentifiers);

    static OnboardingAnswerConstraint unrestricted() {
        return UNRESTRICTED;
    }
}
