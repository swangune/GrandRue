package grandrue.scheduling;

import grandrue.application.MerchantScope;
import grandrue.semantic.AllocationAuthority;
import grandrue.semantic.DomainEvent;

/** Capability-owned consistency boundary for one Appointment mutation. */
public interface AppointmentTransaction
        extends AllocationAuthority, AppointmentMutation {

    MerchantScope merchantScope();

    void appendPendingEvent(DomainEvent pendingEvent);
}
