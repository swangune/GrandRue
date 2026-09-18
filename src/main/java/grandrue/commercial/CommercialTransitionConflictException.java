package grandrue.commercial;

/**
 * Commercial transition conflict caused by an expected-current mismatch.
 *
 * <p>This is the implementation-level expression of the accepted
 * COMMERCIAL_TRANSITION_CONFLICT failure category. Callers must re-read and
 * re-evaluate rather than silently overwriting the committed Commercial state.</p>
 */
public final class CommercialTransitionConflictException extends RuntimeException {

    public CommercialTransitionConflictException(
            String merchantIdentifier,
            String expectedAgreementIdentity,
            String actualAgreementIdentity
    ) {
        super(
                "Commercial transition conflict for merchant "
                        + merchantIdentifier
                        + ": expected current agreement "
                        + String.valueOf(expectedAgreementIdentity)
                        + " but found "
                        + String.valueOf(actualAgreementIdentity)
        );
    }
}
