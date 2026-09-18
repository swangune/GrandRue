package grandrue.scheduling;

import grandrue.application.MerchantScope;
import grandrue.semantic.TimeWindowAllocationScope;

import java.util.Objects;

/** Trusted-scope command for one Appointment commitment. */
public record ConfirmAppointmentCommand(
        MerchantScope merchantScope,
        String identifier,
        String appointmentIdentifier,
        String customerContextIdentifier,
        String scheduledOperationIdentifier,
        TimeWindowAllocationScope scheduledInterval
) {
    public ConfirmAppointmentCommand {
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireIdentifier(identifier, "Command identifier");
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
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
