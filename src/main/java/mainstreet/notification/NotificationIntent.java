package mainstreet.notification;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/** One logical durable communication responsibility under a registered contract. */
public record NotificationIntent(
        String intentIdentity,
        String notificationTypeIdentifier,
        NotificationOwnerScope ownerScope,
        String sourceFactOrProcessReference,
        String recipientResolutionBasisReference,
        Optional<String> correlationIdentifier,
        Optional<String> causationIdentifier,
        Instant createdAt
) {
    public NotificationIntent {
        require(intentIdentity, "Notification Intent identity");
        require(notificationTypeIdentifier, "Notification type");
        Objects.requireNonNull(ownerScope, "ownerScope");
        require(sourceFactOrProcessReference, "Source fact/process reference");
        require(recipientResolutionBasisReference, "Recipient resolution basis reference");
        correlationIdentifier = Objects.requireNonNull(
                correlationIdentifier,
                "correlationIdentifier"
        );
        causationIdentifier = Objects.requireNonNull(
                causationIdentifier,
                "causationIdentifier"
        );
        correlationIdentifier.ifPresent(value -> require(value, "Correlation identity"));
        causationIdentifier.ifPresent(value -> require(value, "Causation identity"));
        Objects.requireNonNull(createdAt, "createdAt");
    }

    private static void require(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
