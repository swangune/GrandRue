package grandrue.scheduling;

import grandrue.application.MerchantScope;
import grandrue.semantic.TimeWindowAllocationScope;

import java.time.Instant;
import java.util.Objects;

/** Appointment-owned input for establishing one authoritative Appointment. */
public record AppointmentConfirmationRequest(
        MerchantScope merchantScope,
        String appointmentIdentifier,
        String customerContextIdentifier,
        String scheduledOperationIdentifier,
        TimeWindowAllocationScope scheduledInterval,
        String allocationClaimIdentifier,
        String governingReleaseIdentifier,
        Instant confirmedAt
) {

    public AppointmentConfirmationRequest {
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireIdentifier(appointmentIdentifier, "Appointment identifier");
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
        Objects.requireNonNull(confirmedAt, "confirmedAt");
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
