package grandrue.scheduling;

import grandrue.customer.CustomerContextAuthority;
import mainstreet.runtime.AuthorizationException;
import grandrue.runtime.CapabilityOperationHandler;
import mainstreet.runtime.OperationExecutionContext;
import mainstreet.runtime.OperationExecutionGuard;
import mainstreet.runtime.OperationFulfilment;
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

/** Application coordinator for one Appointment commitment. */
public final class AppointmentApplicationService
        implements CapabilityOperationHandler<ConfirmAppointmentCommand> {

    private static final String OPERATION_IDENTIFIER = "appointment.confirm";
    private static final ExecutableOperationalObjectTypeIdentity APPOINTMENT_OBJECT =
            new ExecutableOperationalObjectTypeIdentity(
                    "appointment",
                    "appointment"
            );
    private static final List<ExecutableOperationEffect> FULFILLED_EFFECTS =
            List.of(
                    new ExecutableObjectCreationEffect(
                            APPOINTMENT_OBJECT,
                            "confirmed"
                    ),
                    new ExecutableRelationshipEstablishmentEffect(
                            "appointment.customer-context"
                    ),
                    new ExecutableAllocationClaimEffect("appointment.capacity")
            );
    private static final Set<String> COMMITTED_EVENT_IDENTIFIERS =
            Set.of("appointment.confirmed");

    private final AppointmentUnitOfWork unitOfWork;
    private final CustomerContextAuthority customerContexts;
    private final AppointmentSchedulingAuthority scheduling;
    private final OperationExecutionGuard finalAuthorityGuard;
    private final AppointmentConfirmationAuthority confirmationAuthority;
    private final Clock platformClock;

    public AppointmentApplicationService(
            AppointmentUnitOfWork unitOfWork,
            CustomerContextAuthority customerContexts,
            AppointmentSchedulingAuthority scheduling
    ) {
        this(
                unitOfWork,
                customerContexts,
                scheduling,
                (merchantScope, principal, operation) -> {
                    throw new AuthorizationException(
                            "Runtime actor authorisation authority is not configured"
                    );
                },
                AppointmentConfirmationAuthority.standard(),
                Clock.systemUTC()
        );
    }

    public AppointmentApplicationService(
            AppointmentUnitOfWork unitOfWork,
            CustomerContextAuthority customerContexts,
            AppointmentSchedulingAuthority scheduling,
            OperationExecutionGuard finalAuthorityGuard,
            Clock platformClock
    ) {
        this(
                unitOfWork,
                customerContexts,
                scheduling,
                finalAuthorityGuard,
                AppointmentConfirmationAuthority.standard(),
                platformClock
        );
    }

    public AppointmentApplicationService(
            AppointmentUnitOfWork unitOfWork,
            CustomerContextAuthority customerContexts,
            AppointmentSchedulingAuthority scheduling,
            OperationExecutionGuard finalAuthorityGuard,
            AppointmentConfirmationAuthority confirmationAuthority,
            Clock platformClock
    ) {
        this.unitOfWork = Objects.requireNonNull(unitOfWork, "unitOfWork");
        this.customerContexts = Objects.requireNonNull(
                customerContexts,
                "customerContexts"
        );
        this.scheduling = Objects.requireNonNull(scheduling, "scheduling");
        this.finalAuthorityGuard = Objects.requireNonNull(
                finalAuthorityGuard,
                "finalAuthorityGuard"
        );
        this.confirmationAuthority = Objects.requireNonNull(
                confirmationAuthority,
                "confirmationAuthority"
        );
        this.platformClock = Objects.requireNonNull(platformClock, "platformClock");
    }

    @Override
    public OperationFulfilment fulfill(
            ConfirmAppointmentCommand command,
            OperationExecutionContext context
    ) {
        Objects.requireNonNull(command, "command");
        Objects.requireNonNull(context, "context");
        if (!command.merchantScope().equals(context.merchantScope())) {
            throw new IllegalArgumentException(
                    "Appointment command belongs to another merchant scope"
            );
        }
        if (!OPERATION_IDENTIFIER.equals(
                context.applicableOperation().operation().identifier()
        )) {
            throw new IllegalArgumentException(
                    "Appointment handler does not own operation: "
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

    AppointmentConfirmation confirm(ConfirmAppointmentCommand command) {
        return confirm(command, "test-only:unpublished-release", () -> {
            // Direct capability tests exercise persistence independently.
        });
    }

    private AppointmentConfirmation confirm(
            ConfirmAppointmentCommand command,
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
            scheduling.requireAvailable(
                    command.merchantScope(),
                    command.scheduledOperationIdentifier(),
                    command.scheduledInterval()
            );

            Instant recordedAt = platformClock.instant();
            AllocationClaim claim = transaction.claim(
                    command.identifier() + ":allocation",
                    command.scheduledInterval(),
                    command.appointmentIdentifier(),
                    recordedAt
            );
            Appointment appointment = confirmationAuthority.confirm(
                    new AppointmentConfirmationRequest(
                            command.merchantScope(),
                            command.appointmentIdentifier(),
                            command.customerContextIdentifier(),
                            command.scheduledOperationIdentifier(),
                            command.scheduledInterval(),
                            claim.identifier(),
                            governingReleaseIdentifier,
                            recordedAt
                    ),
                    transaction
            );
            DomainEvent event = new DomainEvent(
                    command.identifier() + ":appointment-confirmed",
                    "appointment.confirmed",
                    command.appointmentIdentifier(),
                    command.identifier(),
                    recordedAt
            );
            transaction.appendPendingEvent(event);
            return new AppointmentConfirmation(appointment, claim, event);
        });
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
