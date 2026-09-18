package grandrue.scheduling;

import grandrue.application.MerchantScope;
import mainstreet.semantic.AllocationAuthority;
import mainstreet.semantic.DomainEvent;

/** Capability-owned consistency boundary for one Appointment mutation. */
public interface AppointmentTransaction
        extends AllocationAuthority, AppointmentMutation {

    MerchantScope merchantScope();

    void appendPendingEvent(DomainEvent pendingEvent);
}
