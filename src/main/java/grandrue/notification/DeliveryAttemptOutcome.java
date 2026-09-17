package grandrue.notification;

/**
 * Notification-owned immediate execution certainty. Provider status evidence
 * remains separately recorded as DeliveryEvidence.
 */
public enum DeliveryAttemptOutcome {
    STARTED,
    KNOWN_ACCEPTED,
    KNOWN_REJECTED,
    KNOWN_FAILED_BEFORE_PROVIDER_EFFECT,
    EXECUTION_UNCERTAIN
}
