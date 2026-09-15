package mainstreet.prototype.delivery;

import mainstreet.application.MerchantScope;
import mainstreet.booking.Booking;
import mainstreet.booking.BookingReservationWindow;
import mainstreet.money.CurrencyIdentity;
import mainstreet.money.MonetaryAmount;
import mainstreet.ordering.CommittedQuantity;
import mainstreet.ordering.Order;
import mainstreet.ordering.OrderCommitmentPortion;
import mainstreet.ordering.RequestedOrderPortion;
import mainstreet.prototype.PrototypeAppointmentUseCase;
import mainstreet.prototype.PrototypeBookingUseCase;
import mainstreet.prototype.PrototypeOrderUseCase;
import mainstreet.prototype.PrototypePublicAppointmentUseCase;
import mainstreet.prototype.PrototypePublicBookingUseCase;
import mainstreet.prototype.PrototypePublicOrderUseCase;
import mainstreet.scheduling.Appointment;
import mainstreet.semantic.TimeWindowAllocationScope;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PrototypePublicInteractionControllerTest {

    @Test
    void public_appointment_response_exposes_public_subject_not_internal_execution_context()
            throws Exception {
        MockMvc mvc = MockMvcBuilders.standaloneSetup(
                new PrototypePublicAppointmentController(
                        new PrototypePublicAppointmentUseCase(
                                new AppointmentFixtureUseCase()
                        )
                )
        ).build();

        mvc.perform(post(
                        "/prototype/public/merchants/prototype-gardener-bookable/appointments"
                )
                        .header("Idempotency-Key", "intent-appointment-1")
                        .contentType("application/json")
                        .content("""
                                {
                                  "appointmentIdentifier":"appointment-1",
                                  "subjectReference":"garden-maintenance",
                                  "startsAt":"2026-08-27T13:00:00Z",
                                  "endsAt":"2026-08-27T15:00:00Z"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.appointmentIdentifier")
                        .value("appointment-1"))
                .andExpect(jsonPath("$.subjectReference")
                        .value("garden-maintenance"))
                .andExpect(content().string(not(containsString("gardening.perform"))))
                .andExpect(content().string(not(containsString("customer-1"))))
                .andExpect(content().string(not(containsString("capacity"))))
                .andExpect(content().string(not(containsString("release-1"))));
    }

    @Test
    void public_booking_response_exposes_public_subject_not_internal_booking_context()
            throws Exception {
        MockMvc mvc = MockMvcBuilders.standaloneSetup(
                new PrototypePublicBookingController(
                        new PrototypePublicBookingUseCase(
                                new BookingFixtureUseCase()
                        )
                )
        ).build();

        mvc.perform(post(
                        "/prototype/public/merchants/prototype-daycare/bookings"
                )
                        .header("Idempotency-Key", "intent-booking-1")
                        .contentType("application/json")
                        .content("""
                                {
                                  "bookingIdentifier":"booking-1",
                                  "subjectReference":"full-day-care-session",
                                  "startsAt":"2026-08-27T08:00:00Z",
                                  "endsAt":"2026-08-27T17:00:00Z"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.bookingIdentifier").value("booking-1"))
                .andExpect(jsonPath("$.subjectReference")
                        .value("full-day-care-session"))
                .andExpect(content().string(not(containsString("daycare-session"))))
                .andExpect(content().string(not(containsString("customer-1"))))
                .andExpect(content().string(not(containsString("release-1"))));
    }

    @Test
    void public_order_response_exposes_public_proposition_not_internal_sku_or_provenance()
            throws Exception {
        MockMvc mvc = MockMvcBuilders.standaloneSetup(
                new PrototypePublicOrderController(
                        new PrototypePublicOrderUseCase(new OrderFixtureUseCase())
                )
        ).build();

        mvc.perform(post(
                        "/prototype/public/merchants/prototype-retailer/orders"
                )
                        .header("Idempotency-Key", "intent-order-1")
                        .contentType("application/json")
                        .content("""
                                {
                                  "orderIdentifier":"order-1",
                                  "portions":[{
                                    "portionIdentifier":"portion-1",
                                    "subjectReference":"milk-2l",
                                    "quantity":2,
                                    "unitIdentifier":"EACH"
                                  }]
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderIdentifier").value("order-1"))
                .andExpect(jsonPath("$.portions[0].subjectReference")
                        .value("milk-2l"))
                .andExpect(content().string(not(containsString("sku-1"))))
                .andExpect(content().string(not(containsString("prototype-offering"))))
                .andExpect(content().string(not(containsString("release-1"))));
    }

    private static final class AppointmentFixtureUseCase
            implements PrototypeAppointmentUseCase {
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
            return new Appointment(
                    new MerchantScope(merchantIdentifier),
                    appointmentIdentifier,
                    customerContextIdentifier,
                    scheduledOperationIdentifier,
                    new TimeWindowAllocationScope(
                            "prototype-gardener-bookable-capacity-1",
                            startsAt,
                            endsAt
                    ),
                    "allocation-claim-1",
                    "release-1",
                    1,
                    Instant.parse("2026-08-26T08:00:00Z")
            );
        }

        @Override
        public Optional<Appointment> appointment(
                String merchantIdentifier,
                String appointmentIdentifier
        ) {
            return Optional.empty();
        }
    }

    private static final class BookingFixtureUseCase
            implements PrototypeBookingUseCase {
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
            return new Booking(
                    new MerchantScope(merchantIdentifier),
                    bookingIdentifier,
                    customerContextIdentifier,
                    bookedSubjectReference,
                    new BookingReservationWindow(startsAt, endsAt),
                    "release-1",
                    Instant.parse("2026-08-26T08:00:00Z")
            );
        }

        @Override
        public Optional<Booking> booking(
                String merchantIdentifier,
                String bookingIdentifier
        ) {
            return Optional.empty();
        }
    }

    private static final class OrderFixtureUseCase implements PrototypeOrderUseCase {
        @Override
        public Order commit(
                String merchantIdentifier,
                String commandIdentifier,
                String orderIdentifier,
                List<RequestedOrderPortion> requestedPortions
        ) {
            RequestedOrderPortion requested = requestedPortions.getFirst();
            return new Order(
                    new MerchantScope(merchantIdentifier),
                    orderIdentifier,
                    Optional.empty(),
                    List.of(new OrderCommitmentPortion(
                            requested.identifier(),
                            requested.subjectReference(),
                            new CommittedQuantity(
                                    new BigDecimal("2"),
                                    "EACH"
                            ),
                            new MonetaryAmount(
                                    new CurrencyIdentity("GBP"),
                                    BigInteger.valueOf(2500)
                            ),
                            "prototype-offering:sku-1@1"
                    )),
                    "release-1",
                    Instant.parse("2026-08-26T08:00:00Z")
            );
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
