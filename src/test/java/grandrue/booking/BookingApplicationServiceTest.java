package grandrue.booking;

import grandrue.application.MerchantScope;
import grandrue.customer.CustomerContext;
import grandrue.customer.InMemoryCustomerContextAuthority;
import grandrue.semantic.AllocationConflictException;
import grandrue.semantic.TimeWindowAllocationScope;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookingApplicationServiceTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");
    private static final Instant START = Instant.parse("2026-08-28T14:00:00Z");
    private static final Instant END = Instant.parse("2026-08-30T10:00:00Z");
    private static final Instant RECORDED = Instant.parse("2026-08-26T03:00:00Z");

    @Test
    void confirmation_persists_booking_truth_allocation_and_event_without_appointment() {
        InMemoryBookingUnitOfWork unitOfWork = new InMemoryBookingUnitOfWork();
        BookingApplicationService service = service(unitOfWork);

        BookingConfirmation confirmation = service.confirm(command("cmd-1", "booking-1"));

        assertEquals("standard-room", confirmation.booking().bookedSubjectReference());
        assertEquals(
                new BookingReservationWindow(START, END),
                confirmation.booking().reservationWindow()
        );
        assertEquals(
                "prototype-standard-room-capacity-1",
                ((TimeWindowAllocationScope) confirmation.allocationClaim().scope())
                        .subjectIdentifier()
        );
        assertEquals("booking-1", confirmation.allocationClaim().useIdentifier());
        assertEquals("booking.confirmed", confirmation.pendingEvent().factIdentifier());
        assertEquals(RECORDED, confirmation.booking().confirmedAt());
        assertTrue(unitOfWork.booking(MERCHANT, "booking-1").isPresent());
        assertTrue(unitOfWork.pendingEvent(MERCHANT, "cmd-1:booking-confirmed").isPresent());
    }

    @Test
    void identical_command_retry_returns_the_same_authoritative_result() {
        InMemoryBookingUnitOfWork unitOfWork = new InMemoryBookingUnitOfWork();
        BookingApplicationService service = service(unitOfWork);
        ConfirmBookingCommand command = command("cmd-1", "booking-1");

        BookingConfirmation first = service.confirm(command);
        BookingConfirmation retry = service.confirm(command);

        assertSame(first, retry);
        assertEquals(first.booking(), retry.booking());
        assertEquals(first.allocationClaim(), retry.allocationClaim());
    }

    @Test
    void reused_command_identity_with_changed_intent_is_rejected() {
        InMemoryBookingUnitOfWork unitOfWork = new InMemoryBookingUnitOfWork();
        BookingApplicationService service = service(unitOfWork);
        service.confirm(command("cmd-1", "booking-1"));

        ConfirmBookingCommand changed = new ConfirmBookingCommand(
                MERCHANT,
                "cmd-1",
                "booking-2",
                "customer-1",
                "standard-room",
                new BookingReservationWindow(START, END),
                new TimeWindowAllocationScope(
                        "prototype-standard-room-capacity-1",
                        START,
                        END
                )
        );

        assertThrows(
                CommandIdentityConflictException.class,
                () -> service.confirm(changed)
        );
        assertTrue(unitOfWork.booking(MERCHANT, "booking-2").isEmpty());
    }

    @Test
    void overlapping_required_capacity_rolls_back_the_second_booking() {
        InMemoryBookingUnitOfWork unitOfWork = new InMemoryBookingUnitOfWork();
        BookingApplicationService service = service(unitOfWork);
        service.confirm(command("cmd-1", "booking-1"));

        assertThrows(
                AllocationConflictException.class,
                () -> service.confirm(command("cmd-2", "booking-2"))
        );

        assertTrue(unitOfWork.booking(MERCHANT, "booking-2").isEmpty());
        assertTrue(unitOfWork.pendingEvent(MERCHANT, "cmd-2:booking-confirmed").isEmpty());
    }

    @Test
    void customer_relationship_is_required_before_any_mutation() {
        InMemoryBookingUnitOfWork unitOfWork = new InMemoryBookingUnitOfWork();
        InMemoryCustomerContextAuthority customers =
                new InMemoryCustomerContextAuthority();
        BookingApplicationService service = new BookingApplicationService(
                unitOfWork,
                customers,
                (scope, principal, operation) -> { },
                Clock.fixed(RECORDED, ZoneOffset.UTC)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.confirm(command("cmd-1", "booking-1"))
        );
        assertTrue(unitOfWork.booking(MERCHANT, "booking-1").isEmpty());
    }

    @Test
    void allocation_window_must_match_booking_reservation_scope() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new ConfirmBookingCommand(
                        MERCHANT,
                        "cmd-1",
                        "booking-1",
                        "customer-1",
                        "standard-room",
                        new BookingReservationWindow(START, END),
                        new TimeWindowAllocationScope(
                                "capacity-1",
                                START.plusSeconds(3600),
                                END
                        )
                )
        );
    }

    private static BookingApplicationService service(
            InMemoryBookingUnitOfWork unitOfWork
    ) {
        InMemoryCustomerContextAuthority customers =
                new InMemoryCustomerContextAuthority();
        customers.register(new CustomerContext(MERCHANT, "customer-1", Instant.EPOCH));
        return new BookingApplicationService(
                unitOfWork,
                customers,
                (scope, principal, operation) -> { },
                Clock.fixed(RECORDED, ZoneOffset.UTC)
        );
    }

    private static ConfirmBookingCommand command(
            String commandIdentifier,
            String bookingIdentifier
    ) {
        return new ConfirmBookingCommand(
                MERCHANT,
                commandIdentifier,
                bookingIdentifier,
                "customer-1",
                "standard-room",
                new BookingReservationWindow(START, END),
                new TimeWindowAllocationScope(
                        "prototype-standard-room-capacity-1",
                        START,
                        END
                )
        );
    }
}
