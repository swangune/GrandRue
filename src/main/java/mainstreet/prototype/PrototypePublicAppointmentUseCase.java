package mainstreet.prototype;

import mainstreet.scheduling.Appointment;

import java.time.Instant;
import java.util.Objects;

/**
 * Public Appointment application adapter for the prototype storefront.
 *
 * <p>It resolves a public subject reference through the existing
 * Appointment proposition/configuration fixture and then delegates to the
 * already-authoritative Appointment use case. It owns no Appointment or
 * subject-participation semantics.</p>
 */
public final class PrototypePublicAppointmentUseCase {

    private static final String PROTOTYPE_CUSTOMER_CONTEXT = "customer-1";

    private final PrototypeAppointmentUseCase appointments;

    public PrototypePublicAppointmentUseCase(
            PrototypeAppointmentUseCase appointments
    ) {
        this.appointments = Objects.requireNonNull(appointments, "appointments");
    }

    public Appointment confirm(
            String merchantIdentifier,
            String commandIdentifier,
            String appointmentIdentifier,
            String publicSubjectReference,
            Instant startsAt,
            Instant endsAt
    ) {
        PrototypeAppointmentSubjectConfiguration.Subject subject =
                PrototypeAppointmentSubjectConfiguration.byPublicReference(
                                merchantIdentifier,
                                publicSubjectReference
                        )
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Public Appointment subject is not applicable"
                        ));
        return appointments.confirm(
                merchantIdentifier,
                commandIdentifier,
                appointmentIdentifier,
                PROTOTYPE_CUSTOMER_CONTEXT,
                subject.scheduledOperationIdentifier(),
                startsAt,
                endsAt
        );
    }
}
