package grandrue.booking;

import grandrue.application.MerchantScope;
import grandrue.semantic.DomainEvent;

import java.util.Objects;

/**
 * Historical test-only notification prototype.
 *
 * <p>Successful notification delivery deliberately does not mutate Booking's
 * publication responsibility. Production notification progression belongs to
 * registered Notification/Event Reaction contracts, not this fixture.</p>
 */
public final class BookingNotificationDelivery {

    private final BookingOutbox outbox;
    private final BookingNotificationGateway gateway;

    public BookingNotificationDelivery(
            BookingOutbox outbox,
            BookingNotificationGateway gateway
    ) {
        this.outbox = Objects.requireNonNull(outbox);
        this.gateway = Objects.requireNonNull(gateway);
    }

    /** Attempts delivery only for the explicitly selected merchant outbox. */
    public void deliverPending(MerchantScope merchantScope) {
        Objects.requireNonNull(merchantScope);
        for (DomainEvent event : outbox.pendingEvents(merchantScope)) {
            try {
                gateway.deliver(event);
            } catch (NotificationDeliveryException ignored) {
                // Historical prototype only: Booking publication stays independent.
            }
        }
    }
}
