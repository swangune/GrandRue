package grandrue.booking;

import grandrue.application.MerchantScope;
import grandrue.semantic.AllocationAuthority;
import grandrue.semantic.DomainEvent;

/** Capability-owned consistency boundary for one merchant's Booking mutation. */
public interface BookingTransaction extends AllocationAuthority {

    MerchantScope merchantScope();

    void recordBooking(Booking booking);

    void appendPendingEvent(DomainEvent pendingEvent);
}
