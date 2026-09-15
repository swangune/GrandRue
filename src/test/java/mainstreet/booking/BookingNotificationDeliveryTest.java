package mainstreet.booking;

import mainstreet.application.MerchantScope;
import mainstreet.customer.CustomerContext;
import mainstreet.customer.InMemoryCustomerContextAuthority;
import mainstreet.semantic.DomainEvent;
import mainstreet.semantic.TimeWindowAllocationScope;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BookingNotificationDeliveryTest {

    private static final MerchantScope MERCHANT_SCOPE =
            new MerchantScope("merchant-a");
    private static final Instant START =
            Instant.parse("2026-08-20T14:00:00Z");
    private static final Instant END =
            Instant.parse("2026-08-22T10:00:00Z");
    private static final Instant CUSTOMER_CONTEXT_RECORDED_AT =
            Instant.parse("2026-08-20T10:16:00Z");

    @Test
    void retryable_failure_retains_event_without_reversing_booking() {
        InMemoryBookingUnitOfWork unitOfWork = new InMemoryBookingUnitOfWork();
        BookingConfirmation confirmation = confirm(unitOfWork);
        RecordingGateway gateway = new RecordingGateway();
        gateway.failRetryably = true;
        BookingNotificationDelivery delivery =
                new BookingNotificationDelivery(unitOfWork, gateway);

        delivery.deliverPending(MERCHANT_SCOPE);

        assertSame(
                confirmation.booking(),
                unitOfWork.booking(MERCHANT_SCOPE, "booking-123").orElseThrow()
        );
        assertSame(
                confirmation.allocationClaim(),
                unitOfWork.conflictingClaim(
                        MERCHANT_SCOPE,
                        confirmation.allocationClaim().scope()
                ).orElseThrow()
        );
        assertSame(
                confirmation.pendingEvent(),
                unitOfWork.pendingEvent(
                        MERCHANT_SCOPE,
                        confirmation.pendingEvent().identifier()
                ).orElseThrow()
        );
    }

    @Test
    void successful_notification_delivery_does_not_complete_event_publication() {
        InMemoryBookingUnitOfWork unitOfWork = new InMemoryBookingUnitOfWork();
        BookingConfirmation confirmation = confirm(unitOfWork);
        RecordingGateway gateway = new RecordingGateway();
        BookingNotificationDelivery delivery =
                new BookingNotificationDelivery(unitOfWork, gateway);

        gateway.failRetryably = true;
        delivery.deliverPending(MERCHANT_SCOPE);
        gateway.failRetryably = false;
        delivery.deliverPending(MERCHANT_SCOPE);

        assertEquals(
                List.of(
                        confirmation.pendingEvent().identifier(),
                        confirmation.pendingEvent().identifier()
                ),
                gateway.attemptedEventIdentifiers
        );
        assertSame(
                confirmation.pendingEvent(),
                unitOfWork.pendingEvent(
                        MERCHANT_SCOPE,
                        confirmation.pendingEvent().identifier()
                ).orElseThrow()
        );
        assertSame(
                confirmation.booking(),
                unitOfWork.booking(MERCHANT_SCOPE, "booking-123").orElseThrow()
        );
    }

    @Test
    void unexpected_failure_propagates_and_leaves_event_pending() {
        InMemoryBookingUnitOfWork unitOfWork = new InMemoryBookingUnitOfWork();
        BookingConfirmation confirmation = confirm(unitOfWork);
        RecordingGateway gateway = new RecordingGateway();
        gateway.failUnexpectedly = true;
        BookingNotificationDelivery delivery =
                new BookingNotificationDelivery(unitOfWork, gateway);

        assertThrows(
                IllegalStateException.class,
                () -> delivery.deliverPending(MERCHANT_SCOPE)
        );

        assertSame(
                confirmation.pendingEvent(),
                unitOfWork.pendingEvent(
                        MERCHANT_SCOPE,
                        confirmation.pendingEvent().identifier()
                ).orElseThrow()
        );
    }

    @Test
    void delivery_reads_only_selected_merchant_without_completing_owner_publication() {
        InMemoryBookingUnitOfWork unitOfWork = new InMemoryBookingUnitOfWork();
        BookingApplicationService service = bookingService(unitOfWork);
        MerchantScope secondScope = new MerchantScope("merchant-b");
        BookingConfirmation first = service.confirm(command(MERCHANT_SCOPE));
        BookingConfirmation second = service.confirm(command(secondScope));
        RecordingGateway gateway = new RecordingGateway();
        BookingNotificationDelivery delivery =
                new BookingNotificationDelivery(unitOfWork, gateway);

        delivery.deliverPending(MERCHANT_SCOPE);

        assertEquals(
                List.of(first.pendingEvent().identifier()),
                gateway.attemptedEventIdentifiers
        );
        assertSame(
                first.pendingEvent(),
                unitOfWork.pendingEvent(
                        MERCHANT_SCOPE,
                        first.pendingEvent().identifier()
                ).orElseThrow()
        );
        assertSame(
                second.pendingEvent(),
                unitOfWork.pendingEvent(
                        secondScope,
                        second.pendingEvent().identifier()
                ).orElseThrow()
        );
    }

    private static BookingConfirmation confirm(
            InMemoryBookingUnitOfWork unitOfWork
    ) {
        return bookingService(unitOfWork).confirm(command(MERCHANT_SCOPE));
    }

    private static ConfirmBookingCommand command(MerchantScope scope) {
        return new ConfirmBookingCommand(
                scope,
                "command-001",
                "booking-123",
                "customer-123",
                "standard-room",
                new BookingReservationWindow(START, END),
                new TimeWindowAllocationScope(
                        "standard-room-capacity-1",
                        START,
                        END
                )
        );
    }

    private static BookingApplicationService bookingService(
            BookingUnitOfWork unitOfWork
    ) {
        InMemoryCustomerContextAuthority customerContexts =
                new InMemoryCustomerContextAuthority();
        customerContexts.register(new CustomerContext(
                MERCHANT_SCOPE,
                "customer-123",
                CUSTOMER_CONTEXT_RECORDED_AT
        ));
        customerContexts.register(new CustomerContext(
                new MerchantScope("merchant-b"),
                "customer-123",
                CUSTOMER_CONTEXT_RECORDED_AT
        ));
        return new BookingApplicationService(unitOfWork, customerContexts);
    }

    private static final class RecordingGateway
            implements BookingNotificationGateway {

        private final List<String> attemptedEventIdentifiers = new ArrayList<>();
        private boolean failRetryably;
        private boolean failUnexpectedly;

        @Override
        public void deliver(DomainEvent event) {
            attemptedEventIdentifiers.add(event.identifier());
            if (failUnexpectedly) {
                throw new IllegalStateException("Unexpected notification defect");
            }
            if (failRetryably) {
                throw new NotificationDeliveryException(
                        "Notification provider unavailable"
                );
            }
        }
    }
}
