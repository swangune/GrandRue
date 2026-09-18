package grandrue.scheduling;

import grandrue.application.MerchantScope;
import mainstreet.semantic.TimeWindowAllocationScope;

import java.time.Instant;
import java.util.Objects;

/**
 * Appointment-owned authoritative customer service-time commitment. Scheduling
 * validates the interval and Calendar may represent it; neither owns this
 * Operational Object and Booking is not universally required.
 */
public record Appointment(
        MerchantScope merchantScope,
        String identifier,
        String customerContextIdentifier,
        String scheduledOperationIdentifier,
        TimeWindowAllocationScope scheduledInterval,
        String allocationClaimIdentifier,
        String governingReleaseIdentifier,
        long revision,
        Instant confirmedAt
) {

    public Appointment {
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireIdentifier(identifier, "Appointment identifier");
        requireIdentifier(
                customerContextIdentifier,
                "Customer context identifier"
        );
        requireIdentifier(
                scheduledOperationIdentifier,
                "Scheduled operation identifier"
        );
        Objects.requireNonNull(scheduledInterval, "scheduledInterval");
        requireIdentifier(
                allocationClaimIdentifier,
                "Allocation claim identifier"
        );
        requireIdentifier(
                governingReleaseIdentifier,
                "Governing release identifier"
        );
        if (revision < 1) {
            throw new IllegalArgumentException(
                    "Appointment revision must be positive"
            );
        }
        Objects.requireNonNull(confirmedAt, "confirmedAt");
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
