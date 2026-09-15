package mainstreet.onboarding;

/**
 * Raised when a material onboarding mutation was prepared against a case revision that is no
 * longer current.
 *
 * <p>Callers must re-read and reconcile the merchant's intended change rather than silently
 * overwriting the committed onboarding evidence state.</p>
 */
public final class OnboardingCaseRevisionConflictException extends RuntimeException {

    public OnboardingCaseRevisionConflictException(
            OnboardingCaseIdentity onboardingCaseIdentity,
            OnboardingCaseRevision expectedRevision,
            OnboardingCaseRevision actualRevision
    ) {
        super(
                "Onboarding case revision conflict for "
                        + onboardingCaseIdentity.value()
                        + ": expected "
                        + expectedRevision.value()
                        + " but found "
                        + actualRevision.value()
        );
    }
}
