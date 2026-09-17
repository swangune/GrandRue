package grandrue.notification;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/** One physical provider-path attempt belonging to a stable Dispatch. */
public record DeliveryAttempt(
        String attemptIdentity,
        String dispatchIdentity,
        NotificationOwnerScope ownerScope,
        Instant attemptedAt,
        String channelProviderContextReference,
        String providerIdempotencyReference,
        DeliveryAttemptOutcome outcome,
        Optional<String> failureReference
) {
    public DeliveryAttempt {
        require(attemptIdentity, "Delivery Attempt identity");
        require(dispatchIdentity, "Dispatch identity");
        Objects.requireNonNull(ownerScope, "ownerScope");
        Objects.requireNonNull(attemptedAt, "attemptedAt");
        require(channelProviderContextReference, "Channel/provider context reference");
        require(providerIdempotencyReference, "Provider idempotency reference");
        Objects.requireNonNull(outcome, "outcome");
        failureReference = Objects.requireNonNull(
                failureReference,
                "failureReference"
        );
        failureReference.ifPresent(value -> require(value, "Failure reference"));
    }

    public boolean terminal() {
        return outcome != DeliveryAttemptOutcome.STARTED;
    }

    private static void require(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
