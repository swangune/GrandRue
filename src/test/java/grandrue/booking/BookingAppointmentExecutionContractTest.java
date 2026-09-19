package grandrue.booking;

import grandrue.scheduling.Appointment;
import org.junit.jupiter.api.Test;

import java.lang.reflect.RecordComponent;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class BookingAppointmentExecutionContractTest {

    @Test
    void booking_command_carries_booking_truth_without_appointment_semantics() {
        assertEquals(
                Set.of(
                        "merchantScope",
                        "identifier",
                        "bookingIdentifier",
                        "customerContextIdentifier",
                        "bookedSubjectReference",
                        "reservationWindow",
                        "allocationScope"
                ),
                componentNames(ConfirmBookingCommand.class)
        );
        assertFalse(componentNames(ConfirmBookingCommand.class)
                .contains("appointmentIdentifier"));
        assertFalse(componentNames(ConfirmBookingCommand.class)
                .contains("scheduledOperationIdentifier"));
    }

    @Test
    void appointment_is_not_structurally_owned_by_booking() {
        assertFalse(componentNames(Appointment.class).contains("bookingIdentifier"));
    }

    private static Set<String> componentNames(Class<?> recordType) {
        return Arrays.stream(recordType.getRecordComponents())
                .map(RecordComponent::getName)
                .collect(Collectors.toUnmodifiableSet());
    }
}
