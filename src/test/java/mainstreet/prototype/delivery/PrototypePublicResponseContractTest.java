package mainstreet.prototype.delivery;

import mainstreet.application.MerchantScope;
import mainstreet.booking.Booking;
import mainstreet.booking.BookingReservationWindow;
import mainstreet.money.CurrencyIdentity;
import mainstreet.money.MonetaryAmount;
import mainstreet.ordering.CommittedQuantity;
import mainstreet.ordering.Order;
import mainstreet.ordering.OrderCommitmentPortion;
import mainstreet.scheduling.Appointment;
import mainstreet.semantic.TimeWindowAllocationScope;
import org.junit.jupiter.api.Test;

import java.lang.reflect.RecordComponent;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class PrototypePublicResponseContractTest {

    @Test
    void public_appointment_receipt_exposes_no_internal_execution_context() {
        assertEquals(
                List.of(
                        "merchantIdentifier",
                        "appointmentIdentifier",
                        "subjectReference",
                        "startsAt",
                        "endsAt",
                        "confirmedAt"
                ),
                componentNames(PrototypePublicAppointmentResponse.class)
        );
    }

    @Test
    void public_booking_receipt_exposes_no_internal_booking_or_allocation_context() {
        assertEquals(
                List.of(
                        "merchantIdentifier",
                        "bookingIdentifier",
                        "subjectReference",
                        "startsAt",
                        "endsAt",
                        "confirmedAt"
                ),
                componentNames(PrototypePublicBookingResponse.class)
        );
    }

    @Test
    void public_order_receipt_exposes_public_subject_and_quantity_only() {
        assertEquals(
                List.of(
                        "merchantIdentifier",
                        "orderIdentifier",
                        "committedAt",
                        "portions"
                ),
                componentNames(PrototypePublicOrderResponse.class)
        );
        assertEquals(
                List.of(
                        "portionIdentifier",
                        "subjectReference",
                        "quantity"
                ),
                componentNames(PrototypePublicOrderResponse.Portion.class)
        );
    }

    @Test
    void appointment_and_booking_receipts_use_public_subject_references() {
        Instant startsAt = Instant.parse("2026-08-27T12:00:00Z");
        Instant endsAt = Instant.parse("2026-08-27T14:00:00Z");
        Instant confirmedAt = Instant.parse("2026-08-26T08:00:00Z");

        Appointment appointment = new Appointment(
                new MerchantScope("prototype-gardener-bookable"),
                "appointment-1",
                "customer-1",
                "gardening.perform",
                new TimeWindowAllocationScope(
                        "prototype-gardener-bookable-capacity-1",
                        startsAt,
                        endsAt
                ),
                "claim-1",
                "release-1",
                1,
                confirmedAt
        );
        PrototypePublicAppointmentResponse appointmentReceipt =
                PrototypePublicAppointmentResponse.from(
                        appointment,
                        "garden-maintenance"
                );
        assertEquals("garden-maintenance", appointmentReceipt.subjectReference());
        assertFalse(appointmentReceipt.toString().contains("gardening.perform"));
        assertFalse(appointmentReceipt.toString().contains("customer-1"));
        assertFalse(appointmentReceipt.toString().contains("capacity-1"));

        Booking booking = new Booking(
                new MerchantScope("prototype-daycare"),
                "booking-1",
                "customer-1",
                "daycare-session",
                new BookingReservationWindow(startsAt, endsAt),
                "release-1",
                confirmedAt
        );
        PrototypePublicBookingResponse bookingReceipt =
                PrototypePublicBookingResponse.from(
                        booking,
                        "full-day-care-session"
                );
        assertEquals("full-day-care-session", bookingReceipt.subjectReference());
        assertFalse(bookingReceipt.toString().contains("daycare-session"));
        assertFalse(bookingReceipt.toString().contains("customer-1"));
    }

    @Test
    void order_receipt_translates_internal_orderable_subject_to_public_proposition() {
        Order order = new Order(
                new MerchantScope("prototype-retailer"),
                "order-1",
                Optional.empty(),
                List.of(new OrderCommitmentPortion(
                        "portion-1",
                        "sku-1",
                        new CommittedQuantity(BigDecimal.valueOf(2), "EACH"),
                        new MonetaryAmount(
                                new CurrencyIdentity("GBP"),
                                BigInteger.valueOf(2500)
                        ),
                        "prototype-offering:sku-1@1"
                )),
                "release-1",
                Instant.parse("2026-08-26T08:00:00Z")
        );
        PrototypePublicOrderRequest request = new PrototypePublicOrderRequest(
                "order-1",
                List.of(new PrototypePublicOrderRequest.Portion(
                        "portion-1",
                        "milk-2l",
                        BigDecimal.valueOf(2)
                ))
        );

        PrototypePublicOrderResponse receipt = PrototypePublicOrderResponse.from(
                order,
                request
        );
        assertEquals("milk-2l", receipt.portions().getFirst().subjectReference());
        assertEquals(BigDecimal.valueOf(2), receipt.portions().getFirst().quantity());
        assertFalse(receipt.toString().contains("sku-1"));
        assertFalse(receipt.toString().contains("EACH"));
        assertFalse(receipt.toString().contains("GBP"));
        assertFalse(receipt.toString().contains("prototype-offering"));
    }

    @Test
    void prohibited_internal_transport_names_are_absent_from_all_public_receipts() {
        List<String> names = List.of(
                PrototypePublicAppointmentResponse.class,
                PrototypePublicBookingResponse.class,
                PrototypePublicOrderResponse.class,
                PrototypePublicOrderResponse.Portion.class
        ).stream().flatMap(type -> componentNames(type).stream()).toList();

        for (String prohibited : List.of(
                "customerContextIdentifier",
                "scheduledOperationIdentifier",
                "bookedSubjectReference",
                "allocationClaimIdentifier",
                "governingReleaseIdentifier",
                "unitIdentifier",
                "currencyIdentifier",
                "commercialTermsProvenanceReference"
        )) {
            assertFalse(names.contains(prohibited), prohibited);
        }
    }

    private static List<String> componentNames(Class<?> recordType) {
        return Arrays.stream(recordType.getRecordComponents())
                .map(RecordComponent::getName)
                .toList();
    }
}
