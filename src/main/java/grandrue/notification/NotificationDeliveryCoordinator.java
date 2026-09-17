package grandrue.notification;

import java.time.Clock;
import java.util.Objects;
import java.util.Optional;

/**
 * Application-level delivery orchestration for one already-established
 * Notification Dispatch.
 *
 * <p>Current applicability/recipient/exposure authority is revalidated before
 * externalisation. The Dispatch identity is propagated as the stable provider
 * idempotency reference. Existing STARTED or terminal attempts are never
 * blindly re-executed.</p>
 */
public final class NotificationDeliveryCoordinator {

    private final NotificationStore store;
    private final NotificationDispatchCurrentEligibility eligibility;
    private final NotificationChannelDeliveryPort deliveryPort;
    private final Clock clock;

    public NotificationDeliveryCoordinator(
            NotificationStore store,
            NotificationDispatchCurrentEligibility eligibility,
            NotificationChannelDeliveryPort deliveryPort,
            Clock clock
    ) {
        this.store = Objects.requireNonNull(store, "store");
        this.eligibility = Objects.requireNonNull(eligibility, "eligibility");
        this.deliveryPort = Objects.requireNonNull(deliveryPort, "deliveryPort");
        this.clock = Objects.requireNonNull(clock, "clock");
    }

    public DeliveryAttempt deliver(
            String dispatchIdentity,
            String attemptIdentity,
            String channelProviderContextReference
    ) {
        require(dispatchIdentity, "Dispatch identity");
        require(attemptIdentity, "Attempt identity");
        require(channelProviderContextReference, "Channel/provider context reference");

        DeliveryAttempt existing = store.findAttempt(attemptIdentity).orElse(null);
        if (existing != null) {
            if (!existing.dispatchIdentity().equals(dispatchIdentity)) {
                throw new IllegalArgumentException(
                        "Delivery Attempt identity belongs to another Dispatch"
                );
            }
            return existing;
        }

        NotificationDispatch dispatch = store.findDispatch(dispatchIdentity)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown Notification Dispatch: " + dispatchIdentity
                ));
        if (!eligibility.mayExternalise(dispatch)) {
            throw new NotificationDispatchNotEligibleException(dispatchIdentity);
        }

        String providerIdempotencyReference = dispatch.dispatchIdentity();
        DeliveryAttempt started = store.startAttempt(new DeliveryAttempt(
                attemptIdentity,
                dispatchIdentity,
                dispatch.ownerScope(),
                clock.instant(),
                channelProviderContextReference,
                providerIdempotencyReference,
                DeliveryAttemptOutcome.STARTED,
                Optional.empty()
        ));
        if (started.terminal()) {
            return started;
        }

        NotificationDeliveryProviderResult providerResult;
        try {
            providerResult = Objects.requireNonNull(
                    deliveryPort.deliver(dispatch, providerIdempotencyReference),
                    "Notification delivery port returned null"
            );
        } catch (RuntimeException providerFailure) {
            return store.completeAttempt(new DeliveryAttempt(
                    started.attemptIdentity(),
                    started.dispatchIdentity(),
                    started.ownerScope(),
                    started.attemptedAt(),
                    started.channelProviderContextReference(),
                    started.providerIdempotencyReference(),
                    DeliveryAttemptOutcome.EXECUTION_UNCERTAIN,
                    Optional.of(
                            "provider-exception:"
                                    + providerFailure.getClass().getName()
                    )
            ));
        }

        return store.completeAttempt(new DeliveryAttempt(
                started.attemptIdentity(),
                started.dispatchIdentity(),
                started.ownerScope(),
                started.attemptedAt(),
                started.channelProviderContextReference(),
                started.providerIdempotencyReference(),
                providerResult.outcome(),
                providerResult.failureReference()
        ));
    }

    private static void require(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
