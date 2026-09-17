package grandrue.notification;

import java.util.List;
import java.util.Optional;

/** Persistence/idempotency boundary for Notification-owned durable facts. */
public interface NotificationStore {
    NotificationIntent establishIntent(NotificationIntent intent);
    NotificationDispatch establishDispatch(NotificationDispatch dispatch);
    DeliveryAttempt startAttempt(DeliveryAttempt attempt);
    DeliveryAttempt completeAttempt(DeliveryAttempt attempt);
    DeliveryEvidence recordEvidence(DeliveryEvidence evidence);
    Optional<NotificationIntent> findIntent(String intentIdentity);
    Optional<NotificationDispatch> findDispatch(String dispatchIdentity);
    Optional<DeliveryAttempt> findAttempt(String attemptIdentity);
    List<DeliveryAttempt> attemptsForDispatch(String dispatchIdentity);
}
