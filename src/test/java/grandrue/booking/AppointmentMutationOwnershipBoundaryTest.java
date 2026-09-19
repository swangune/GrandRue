package grandrue.booking;

import grandrue.application.MerchantScope;
import grandrue.scheduling.Appointment;
import grandrue.scheduling.AppointmentConfirmationAuthority;
import grandrue.scheduling.AppointmentConfirmationRequest;
import grandrue.scheduling.AppointmentMutation;
import grandrue.scheduling.AppointmentTransaction;
import grandrue.semantic.TimeWindowAllocationScope;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AppointmentMutationOwnershipBoundaryTest {

    @Test
    void appointment_transaction_owns_appointment_mutation_and_booking_transaction_does_not() {
        assertTrue(AppointmentMutation.class.isAssignableFrom(
                AppointmentTransaction.class
        ));
        assertFalse(AppointmentMutation.class.isAssignableFrom(
                BookingTransaction.class
        ));
        assertFalse(Arrays.stream(BookingTransaction.class.getDeclaredMethods())
                .map(Method::getName)
                .anyMatch("recordAppointment"::equals));
    }

    @Test
    void appointment_confirmation_authority_owns_creation_and_staging_without_booking() {
        RecordingAppointmentMutation mutation =
                new RecordingAppointmentMutation();
        AppointmentConfirmationAuthority authority =
                AppointmentConfirmationAuthority.standard();

        Appointment appointment = authority.confirm(
                new AppointmentConfirmationRequest(
                        new MerchantScope("merchant-a"),
                        "appointment-123",
                        "customer-123",
                        "consultation.perform",
                        new TimeWindowAllocationScope(
                                "consultant-capacity-1",
                                Instant.parse("2026-08-20T14:00:00Z"),
                                Instant.parse("2026-08-20T14:30:00Z")
                        ),
                        "allocation-123",
                        "configuration-release-1",
                        Instant.parse("2026-08-20T10:16:00Z")
                ),
                mutation
        );

        assertEquals(appointment, mutation.recorded);
        assertEquals("appointment-123", appointment.identifier());
        assertEquals("customer-123", appointment.customerContextIdentifier());
        assertEquals("consultation.perform", appointment.scheduledOperationIdentifier());
    }

    private static final class RecordingAppointmentMutation
            implements AppointmentMutation {

        private Appointment recorded;

        @Override
        public void recordAppointment(Appointment appointment) {
            recorded = appointment;
        }
    }
}
