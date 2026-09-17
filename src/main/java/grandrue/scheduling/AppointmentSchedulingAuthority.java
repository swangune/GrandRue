package grandrue.scheduling;

import mainstreet.application.MerchantScope;
import mainstreet.semantic.TimeWindowAllocationScope;

/**
 * Scheduling-owned final revalidation port used immediately before an
 * Appointment commitment is established. Implementations own scheduling
 * constraints; Appointment owns the resulting customer commitment.
 */
@FunctionalInterface
public interface AppointmentSchedulingAuthority {

    void requireAvailable(
            MerchantScope merchantScope,
            String scheduledOperationIdentifier,
            TimeWindowAllocationScope scheduledInterval
    );

    static AppointmentSchedulingAuthority allowAll() {
        return (merchantScope, operation, interval) -> {
            // Useful only where the represented configuration has no
            // additional scheduling constraint beyond capacity.
        };
    }
}
