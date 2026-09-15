package mainstreet.commercial;

/**
 * Rejection raised when a proposed automatic initial trial is not anchored to
 * the Merchant Account's authoritative first committed configuration
 * activation.
 */
public final class InvalidInitialFullExperienceTrialOriginException
        extends IllegalArgumentException {

    public InvalidInitialFullExperienceTrialOriginException() {
        super("Initial trial origin must be the first committed configuration activation");
    }
}
