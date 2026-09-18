package grandrue.booking;

import grandrue.customer.CustomerContextAuthority;
import grandrue.runtime.AuthorizationException;
import grandrue.runtime.CapabilityOperationHandler;
import grandrue.runtime.OperationExecutionContext;
import grandrue.runtime.OperationExecutionGuard;
import grandrue.runtime.OperationFulfilment;
import mainstreet.semantic.AllocationClaim;
import mainstreet.semantic.DomainEvent;
import mainstreet.semantic.executable.ExecutableAllocationClaimEffect;
import mainstreet.semantic.executable.ExecutableObjectCreationEffect;
import mainstreet.semantic.executable.ExecutableOperationalObjectTypeIdentity;
import mainstreet.semantic.executable.ExecutableOperationEffect;
import mainstreet.semantic.executable.ExecutableRelationshipEstablishmentEffect;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/** Application coordinator for the capability-owned Booking confirmation path. */
public final class BookingApplicationService
        implements CapabilityOperationHandler<ConfirmBookingCommand> {

    private static final String OPERATION_IDENTIFIER = "booking.confirm";
    private static final ExecutableOperationalObjectTypeIdentity BOOKING_OBJECT =
            new ExecutableOperationalObjectTypeIdentity("booking", "booking");
    private static final List<ExecutableOperationEffect> FULFILLED_EFFECTS =
            List.of(
                    new ExecutableObjectCreationEffect(
                            BOOKING_OBJECT,
                            "confirmed"
                    ),
                    new ExecutableRelationshipEstablishmentEffect(
                            "booking.customer-context"
                    ),
                    new ExecutableAllocationClaimEffect("booking.capacity")
            );
    private static final Set<String> COMMITTED_EVENT_IDENTIFIERS =
            Set.of("booking.confirmed");

    private final BookingUnitOfWork unitOfWork;
    private final CustomerContextAuthority customerContexts;
    private final OperationExecutionGuard finalAuthorityGuard;
    private final Clock platformClock;

    public BookingApplicationService(
            BookingUnitOfWork unitOfWork,
            CustomerContextAuthority customerContexts
    ) {
        this(
                unitOfWork,
                customerContexts,
                (merchantScope, principal, operation) -> {
                    throw new AuthorizationException(
                            "Runtime actor authorisation authority is not configured"
                    );
                },
                Clock.systemUTC()
        );
    }

    public BookingApplicationService(
            BookingUnitOfWork unitOfWork,
            CustomerContextAuthority customerContexts,
            OperationExecutionGuard finalAuthorityGuard
    ) {
        this(
                unitOfWork,
                customerContexts,
                finalAuthorityGuard,
                Clock.systemUTC()
        );
    }

    public BookingApplicationService(
            BookingUnitOfWork unitOfWork,
            CustomerContextAuthority customerContexts,
            OperationExecutionGuard finalAuthorityGuard,
            Clock platformClock
    ) {
        this.unitOfWork = Objects.requireNonNull(unitOfWork, "unitOfWork");
        this.customerContexts = Objects.requireNonNull(
                customerContexts,
                "customerContexts"
        );
        this.finalAuthorityGuard = Objects.requireNonNull(
                finalAuthorityGuard,
                "finalAuthorityGuard"
        );
        this.platformClock = Objects.requireNonNull(platformClock, "platformClock");
    }

    @Override
    public OperationFulfilment fulfill(
            ConfirmBookingCommand command,
            OperationExecutionContext context
    ) {
        Objects.requireNonNull(command, "command");
        Objects.requireNonNull(context, "context");
        if (!command.merchantScope().equals(context.merchantScope())) {
            throw new IllegalArgumentException(
                    "Booking command belongs to another merchant scope"
            );
        }
        if (!OPERATION_IDENTIFIER.equals(
                context.applicableOperation().operation().identifier()
        )) {
            throw new IllegalArgumentException(
                    "Booking handler does not own operation: "
                            + context.applicableOperation().operation().identifier()
            );
        }

        OperationFulfilment fulfilment = OperationFulfilment.conformingTo(
                context.applicableOperation(),
                FULFILLED_EFFECTS,
                COMMITTED_EVENT_IDENTIFIERS
        );
        confirm(
                command,
                context.applicableOperation().releaseIdentifier(),
                () -> finalAuthorityGuard.validate(
                        context.merchantScope(),
                        context.principal(),
                        context.applicableOperation()
                )
        );
        return fulfilment;
    }

    BookingConfirmation confirm(ConfirmBookingCommand command) {
        return confirm(command, "test-only:unpublished-release");
    }

    BookingConfirmation confirm(
            ConfirmBookingCommand command,
            String governingReleaseIdentifier
    ) {
        return confirm(command, governingReleaseIdentifier, () -> {
            // Direct capability tests exercise persistence independently.
        });
    }

    private BookingConfirmation confirm(
            ConfirmBookingCommand command,
            String governingReleaseIdentifier,
            Runnable finalAuthorityValidation
    ) {
        Objects.requireNonNull(command, "command");
        requireIdentifier(
                governingReleaseIdentifier,
                "Governing release identifier"
        );
        Objects.requireNonNull(finalAuthorityValidation, "finalAuthorityValidation");

        return unitOfWork.execute(command, transaction -> {
            finalAuthorityValidation.run();
            customerContexts.require(
                    command.merchantScope(),
                    command.customerContextIdentifier()
            );

            Instant recordedAt = platformClock.instant();
            Booking booking = new Booking(
                    command.merchantScope(),
                    command.bookingIdentifier(),
                    command.customerContextIdentifier(),
                    command.bookedSubjectReference(),
                    command.reservationWindow(),
                    governingReleaseIdentifier,
                    recordedAt
            );
            transaction.recordBooking(booking);

            AllocationClaim claim = transaction.claim(
                    command.identifier() + ":allocation",
                    command.allocationScope(),
                    command.bookingIdentifier(),
                    recordedAt
            );

            DomainEvent event = new DomainEvent(
                    command.identifier() + ":booking-confirmed",
                    "booking.confirmed",
                    command.bookingIdentifier(),
                    command.identifier(),
                    recordedAt
            );
            transaction.appendPendingEvent(event);

            return new BookingConfirmation(booking, claim, event);
        });
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
