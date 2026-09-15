package mainstreet.notification;

import java.util.Objects;
import java.util.Optional;

/** Provider-neutral immediate result of one physical notification attempt. */
public record NotificationDeliveryProviderResult(
        DeliveryAttemptOutcome outcome,
        Optional<String> failureReference
) {
    public NotificationDeliveryProviderResult {
        Objects.requireNonNull(outcome, "outcome");
        if (outcome == DeliveryAttemptOutcome.STARTED) {
            throw new IllegalArgumentException(
                    "Provider result must resolve STARTED to a terminal certainty"
            );
        }
        failureReference = Objects.requireNonNull(
                failureReference,
                "failureReference"
        );
        failureReference.ifPresent(value -> {
            if (value.isBlank()) {
                throw new IllegalArgumentException(
                        "Provider failure reference must not be blank"
                );
            }
        });
    }
}
