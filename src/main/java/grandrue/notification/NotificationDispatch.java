package grandrue.notification;

import java.time.Instant;
import java.util.Objects;

/** One logical recipient-and-channel obligation, not a physical provider request. */
public record NotificationDispatch(
        String dispatchIdentity,
        String intentIdentity,
        NotificationOwnerScope ownerScope,
        NotificationRecipient recipient,
        NotificationChannel channel,
        String endpointReference,
        String contentProjectionReference,
        Instant createdAt
) {
    public NotificationDispatch {
        require(dispatchIdentity, "Notification Dispatch identity");
        require(intentIdentity, "Notification Intent identity");
        Objects.requireNonNull(ownerScope, "ownerScope");
        Objects.requireNonNull(recipient, "recipient");
        Objects.requireNonNull(channel, "channel");
        require(endpointReference, "Endpoint reference");
        require(contentProjectionReference, "Content projection reference");
        Objects.requireNonNull(createdAt, "createdAt");
    }

    private static void require(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
