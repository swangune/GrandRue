package mainstreet.prototype;

import grandrue.application.MerchantScope;
import grandrue.booking.Booking;
import grandrue.booking.BookingApplicationService;
import grandrue.booking.BookingReservationWindow;
import grandrue.booking.BookingUnitOfWork;
import grandrue.booking.ConfirmBookingCommand;
import grandrue.customer.CustomerContext;
import grandrue.customer.InMemoryCustomerContextAuthority;
import mainstreet.runtime.AuthorizationException;
import mainstreet.runtime.ExecutionPrincipal;
import mainstreet.runtime.OperationExecutionGuard;
import mainstreet.runtime.ScopedOperationDispatcher;
import mainstreet.runtime.TrustedExecutionContext;
import mainstreet.semantic.TimeWindowAllocationScope;
import grandrue.semantic.executable.ActiveOperationResolver;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

/** Durable pre-UI Booking composition for Booking-capable reference merchants. */
public final class PrototypeJooqBookingUseCase
        implements PrototypeBookingUseCase {

    private static final String CUSTOMER = "customer-1";

    private final PrototypeMerchantRuntime merchantRuntime;
    private final BiFunction<MerchantScope, String, Optional<Booking>> bookingReader;
    private final ScopedOperationDispatcher<ConfirmBookingCommand> dispatcher;

    public PrototypeJooqBookingUseCase(
            PrototypeMerchantRuntime merchantRuntime,
            BookingUnitOfWork unitOfWork,
            BiFunction<MerchantScope, String, Optional<Booking>> bookingReader,
            Clock clock
    ) {
        this.merchantRuntime = Objects.requireNonNull(
                merchantRuntime,
                "merchantRuntime"
        );
        BookingUnitOfWork bookingUnitOfWork = Objects.requireNonNull(
                unitOfWork,
                "unitOfWork"
        );
        this.bookingReader = Objects.requireNonNull(bookingReader, "bookingReader");
        Clock platformClock = Objects.requireNonNull(clock, "clock");

        InMemoryCustomerContextAuthority customers =
                new InMemoryCustomerContextAuthority();
        for (String merchantIdentifier :
                PrototypeBookingSubjectConfiguration.merchants()) {
            registerPrototypeCustomer(customers, merchantIdentifier);
        }

        OperationExecutionGuard prototypeAuthority =
                (merchantScope, principal, operation) -> {
                    PrototypeMerchantView merchant = this.merchantRuntime.merchant(
                            merchantScope.merchantIdentifier()
                    );
                    if (!merchant.operationIdentifiers().contains("booking.confirm")
                            || !PrototypeMerchantRuntime.PROTOTYPE_PRINCIPAL.equals(
                                    principal.identifier()
                            )
                            || !"booking.confirm".equals(
                                    operation.operation().identifier()
                            )) {
                        throw new AuthorizationException(
                                "Prototype Booking authority rejected execution"
                        );
                    }
                };

        BookingApplicationService booking = new BookingApplicationService(
                bookingUnitOfWork,
                customers,
                prototypeAuthority,
                platformClock
        );
        this.dispatcher = new ScopedOperationDispatcher<>(
                new ActiveOperationResolver(merchantRuntime.activation()),
                List.of(prototypeAuthority),
                booking
        );
    }

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
        MerchantScope merchantScope = trustedMerchantScope(merchantIdentifier);
        String allocationSubject = allocationSubject(
                merchantScope.merchantIdentifier(),
                bookedSubjectReference
        );
        BookingReservationWindow reservation =
                new BookingReservationWindow(startsAt, endsAt);
        ConfirmBookingCommand command = new ConfirmBookingCommand(
                merchantScope,
                commandIdentifier,
                bookingIdentifier,
                customerContextIdentifier,
                bookedSubjectReference,
                reservation,
                new TimeWindowAllocationScope(
                        allocationSubject,
                        startsAt,
                        endsAt
                )
        );
        dispatcher.dispatch(
                trustedContext(merchantScope),
                "booking.confirm",
                command
        );
        return bookingReader.apply(merchantScope, bookingIdentifier)
                .orElseThrow(() -> new IllegalStateException(
                        "Confirmed prototype Booking is not readable"
                ));
    }

    @Override
    public Optional<Booking> booking(
            String merchantIdentifier,
            String bookingIdentifier
    ) {
        MerchantScope merchantScope = trustedMerchantScope(merchantIdentifier);
        return bookingReader.apply(merchantScope, bookingIdentifier);
    }

    private MerchantScope trustedMerchantScope(String merchantIdentifier) {
        PrototypeMerchantView merchant = merchantRuntime.merchant(merchantIdentifier);
        if (!merchant.operationIdentifiers().contains("booking.confirm")) {
            throw new IllegalArgumentException(
                    "Booking is not exposed for this prototype merchant"
            );
        }
        return new MerchantScope(merchant.merchantIdentifier());
    }

    private static String allocationSubject(
            String merchantIdentifier,
            String bookedSubjectReference
    ) {
        return PrototypeBookingSubjectConfiguration.byBookedSubject(
                        merchantIdentifier,
                        bookedSubjectReference
                )
                .map(PrototypeBookingSubjectConfiguration.Subject::allocationSubjectReference)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown prototype booked subject for merchant: "
                                + bookedSubjectReference
                ));
    }

    private static void registerPrototypeCustomer(
            InMemoryCustomerContextAuthority customers,
            String merchantIdentifier
    ) {
        customers.register(new CustomerContext(
                new MerchantScope(merchantIdentifier),
                CUSTOMER,
                Instant.EPOCH
        ));
    }

    private static TrustedExecutionContext trustedContext(
            MerchantScope merchantScope
    ) {
        return new TrustedExecutionContext(
                merchantScope,
                new ExecutionPrincipal(
                        PrototypeMerchantRuntime.PROTOTYPE_PRINCIPAL
                ),
                Optional.empty()
        );
    }
}
