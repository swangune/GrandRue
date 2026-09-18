package grandrue.booking;

import grandrue.application.MerchantScope;
import mainstreet.semantic.DomainEvent;

import java.util.List;

/**
 * Merchant-scoped durable publication responsibility for Booking-owned events.
 * Event identifiers may be reused by different merchants and therefore never
 * establish tenant scope.
 *
 * <p>Recording publication is technical evidence only. It does not acknowledge
 * any Event Reaction or imply that every consumer completed.</p>
 *
 * <p>Authority: MS-PROT-026 v1.1 — Production Domain Event Publication, Reaction & Consumption Contract Amendment,
 * §11 — Event Publication Is Not Reaction Completion; §12 — Publication Responsibility;
 * §17 — Per-Reaction Acknowledgement.</p>
 */
public interface BookingOutbox {

    List<DomainEvent> pendingEvents(MerchantScope merchantScope);

    void recordPublished(
            MerchantScope merchantScope,
            String eventIdentifier
    );
}
