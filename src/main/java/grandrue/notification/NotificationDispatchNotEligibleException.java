package grandrue.notification;

/** Current applicability/recipient/exposure authority no longer permits delivery. */
public final class NotificationDispatchNotEligibleException
        extends IllegalStateException {
    public NotificationDispatchNotEligibleException(String dispatchIdentity) {
        super("Notification Dispatch is no longer eligible for externalisation: "
                + dispatchIdentity);
    }
}
