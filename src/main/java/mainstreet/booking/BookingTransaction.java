package mainstreet.booking;

import mainstreet.application.MerchantScope;
import mainstreet.semantic.AllocationAuthority;
import mainstreet.semantic.DomainEvent;

/** Capability-owned consistency boundary for one merchant's Booking mutation. */
public interface BookingTransaction extends AllocationAuthority {

    MerchantScope merchantScope();

    void recordBooking(Booking booking);

    void appendPendingEvent(DomainEvent pendingEvent);
}
