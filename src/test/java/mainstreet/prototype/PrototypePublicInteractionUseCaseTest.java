package mainstreet.prototype;

import mainstreet.booking.Booking;
import mainstreet.ordering.CommittedQuantity;
import mainstreet.ordering.Order;
import mainstreet.ordering.RequestedOrderPortion;
import mainstreet.scheduling.Appointment;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PrototypePublicInteractionUseCaseTest {

    @Test
    void appointment_public_subject_resolves_to_owned_internal_operation_context() {
        RecordingAppointmentUseCase delegate = new RecordingAppointmentUseCase();
        PrototypePublicAppointmentUseCase publicUseCase =
                new PrototypePublicAppointmentUseCase(delegate);

        publicUseCase.confirm(
                "prototype-gardener-bookable",
                "intent-1",
                "appointment-1",
                "garden-maintenance",
                Instant.parse("2026-08-27T13:00:00Z"),
                Instant.parse("2026-08-27T15:00:00Z")
        );

        assertEquals("customer-1", delegate.customerContextIdentifier);
        assertEquals("gardening.perform", delegate.scheduledOperationIdentifier);
    }

    @Test
    void booking_public_subject_resolves_without_exposing_allocation_identity() {
        RecordingBookingUseCase delegate = new RecordingBookingUseCase();
        PrototypePublicBookingUseCase publicUseCase =
                new PrototypePublicBookingUseCase(delegate);

        publicUseCase.confirm(
                "prototype-daycare",
                "intent-2",
                "booking-1",
                "full-day-care-session",
                Instant.parse("2026-08-27T08:00:00Z"),
                Instant.parse("2026-08-27T17:00:00Z")
        );

        assertEquals("customer-1", delegate.customerContextIdentifier);
        assertEquals("daycare-session", delegate.bookedSubjectReference);
    }

    @Test
    void ordering_public_subject_resolves_subject_and_configured_quantity_unit() {
        RecordingOrderUseCase delegate = new RecordingOrderUseCase();
        PrototypePublicOrderUseCase publicUseCase =
                new PrototypePublicOrderUseCase(delegate);

        publicUseCase.commit(
                "prototype-retailer",
                "intent-3",
                "order-1",
                List.of(new PrototypePublicOrderPortion(
                        "portion-1",
                        "milk-2l",
                        BigDecimal.valueOf(2)
                ))
        );

        assertEquals(1, delegate.portions.size());
        assertEquals("sku-1", delegate.portions.getFirst().subjectReference());
        assertEquals(
                new CommittedQuantity(BigDecimal.valueOf(2), "EACH"),
                delegate.portions.getFirst().quantity()
        );
    }

    @Test
    void unknown_or_cross_context_public_subject_fails_closed_before_execution() {
        RecordingAppointmentUseCase appointmentDelegate =
                new RecordingAppointmentUseCase();
        PrototypePublicAppointmentUseCase appointments =
                new PrototypePublicAppointmentUseCase(appointmentDelegate);

        assertThrows(
                IllegalArgumentException.class,
                () -> appointments.confirm(
                        "prototype-gardener-showcase",
                        "intent-4",
                        "appointment-2",
                        "garden-maintenance",
                        Instant.parse("2026-08-27T13:00:00Z"),
                        Instant.parse("2026-08-27T15:00:00Z")
                )
        );
        assertEquals(0, appointmentDelegate.invocations);
    }

    private static final class RecordingAppointmentUseCase
            implements PrototypeAppointmentUseCase {
        private int invocations;
        private String customerContextIdentifier;
        private String scheduledOperationIdentifier;

        @Override
        public Appointment confirm(
                String merchantIdentifier,
                String commandIdentifier,
                String appointmentIdentifier,
                String customerContextIdentifier,
                String scheduledOperationIdentifier,
                Instant startsAt,
                Instant endsAt
        ) {
            invocations++;
            this.customerContextIdentifier = customerContextIdentifier;
            this.scheduledOperationIdentifier = scheduledOperationIdentifier;
            return null;
        }

        @Override
        public Optional<Appointment> appointment(
                String merchantIdentifier,
                String appointmentIdentifier
        ) {
            return Optional.empty();
        }
    }

    private static final class RecordingBookingUseCase
            implements PrototypeBookingUseCase {
        private String customerContextIdentifier;
        private String bookedSubjectReference;

        @Override
        public Booking confirm(
                String merchantIdentifier,
                String commandIdentifier,
                String bookingIdentifier,
                String customerContextIdentifier,
                String bookedSubjectReference,
                Instant startsAt,
                Instant endsAt
        ) {
            this.customerContextIdentifier = customerContextIdentifier;
            this.bookedSubjectReference = bookedSubjectReference;
            return null;
        }

        @Override
        public Optional<Booking> booking(
                String merchantIdentifier,
                String bookingIdentifier
        ) {
            return Optional.empty();
        }
    }

    private static final class RecordingOrderUseCase implements PrototypeOrderUseCase {
        private List<RequestedOrderPortion> portions = List.of();

        @Override
        public Order commit(
                String merchantIdentifier,
                String commandIdentifier,
                String orderIdentifier,
                List<RequestedOrderPortion> requestedPortions
        ) {
            this.portions = List.copyOf(requestedPortions);
            return null;
        }

        @Override
        public Optional<Order> order(
                String merchantIdentifier,
                String orderIdentifier
        ) {
            return Optional.empty();
        }
    }
}
