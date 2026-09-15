package mainstreet.prototype.delivery;

import java.time.Instant;
import java.util.Objects;

/** Public storefront Appointment intent; contains no internal operation identifier. */
public record PrototypePublicAppointmentRequest(
        String appointmentIdentifier,
        String subjectReference,
        Instant startsAt,
        Instant endsAt
) {
    public PrototypePublicAppointmentRequest {
        requireIdentifier(appointmentIdentifier, "Appointment identifier");
        requireIdentifier(subjectReference, "Subject reference");
        Objects.requireNonNull(startsAt, "startsAt");
        Objects.requireNonNull(endsAt, "endsAt");
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
