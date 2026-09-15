package mainstreet.prototype.delivery;

import java.time.Instant;

public record PrototypeAppointmentRequest(
        String appointmentIdentifier,
        String customerContextIdentifier,
        String scheduledOperationIdentifier,
        Instant startsAt,
        Instant endsAt
) {
    public PrototypeAppointmentRequest {
        requireIdentifier(appointmentIdentifier, "Appointment identifier");
        requireIdentifier(
                customerContextIdentifier,
                "Customer context identifier"
        );
        requireIdentifier(
                scheduledOperationIdentifier,
                "Scheduled operation identifier"
        );
        if (startsAt == null || endsAt == null || !endsAt.isAfter(startsAt)) {
            throw new IllegalArgumentException(
                    "Appointment interval must have a positive duration"
            );
        }
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
