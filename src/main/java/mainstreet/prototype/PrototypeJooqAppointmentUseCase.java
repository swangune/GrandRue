package mainstreet.prototype;

import mainstreet.application.MerchantScope;
import grandrue.customer.CustomerContext;
import grandrue.customer.InMemoryCustomerContextAuthority;
import mainstreet.runtime.AuthorizationException;
import mainstreet.runtime.ExecutionPrincipal;
import mainstreet.runtime.OperationExecutionGuard;
import mainstreet.runtime.ScopedOperationDispatcher;
import mainstreet.runtime.TrustedExecutionContext;
import mainstreet.semantic.TimeWindowAllocationScope;
import mainstreet.semantic.executable.ActiveOperationResolver;
import grandrue.scheduling.Appointment;
import grandrue.scheduling.AppointmentApplicationService;
import grandrue.scheduling.AppointmentSchedulingAuthority;
import grandrue.scheduling.AppointmentUnitOfWork;
import grandrue.scheduling.ConfirmAppointmentCommand;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

/** Durable pre-UI Appointment composition for Appointment-capable merchants. */
public final class PrototypeJooqAppointmentUseCase
        implements PrototypeAppointmentUseCase {

    private static final String CUSTOMER = "customer-1";

    private final PrototypeMerchantRuntime merchantRuntime;
    private final BiFunction<MerchantScope, String, Optional<Appointment>> appointmentReader;
    private final ScopedOperationDispatcher<ConfirmAppointmentCommand> dispatcher;

    public PrototypeJooqAppointmentUseCase(
            PrototypeMerchantRuntime merchantRuntime,
            AppointmentUnitOfWork unitOfWork,
            BiFunction<MerchantScope, String, Optional<Appointment>> appointmentReader,
            Clock clock
    ) {
        this.merchantRuntime = Objects.requireNonNull(
                merchantRuntime,
                "merchantRuntime"
        );
        AppointmentUnitOfWork appointmentUnitOfWork = Objects.requireNonNull(
                unitOfWork,
                "unitOfWork"
        );
        this.appointmentReader = Objects.requireNonNull(
                appointmentReader,
                "appointmentReader"
        );
        Clock platformClock = Objects.requireNonNull(clock, "clock");

        InMemoryCustomerContextAuthority customers =
                new InMemoryCustomerContextAuthority();
        for (String merchantIdentifier :
                PrototypeAppointmentSubjectConfiguration.merchants()) {
            registerPrototypeCustomer(customers, merchantIdentifier);
        }

        OperationExecutionGuard prototypeAuthority =
                (merchantScope, principal, operation) -> {
                    PrototypeMerchantView merchant = this.merchantRuntime.merchant(
                            merchantScope.merchantIdentifier()
                    );
                    if (!merchant.operationIdentifiers().contains("appointment.confirm")
                            || !PrototypeMerchantRuntime.PROTOTYPE_PRINCIPAL.equals(
                                    principal.identifier()
                            )
                            || !"appointment.confirm".equals(
                                    operation.operation().identifier()
                            )) {
                        throw new AuthorizationException(
                                "Prototype Appointment authority rejected execution"
                        );
                    }
                };

        AppointmentSchedulingAuthority scheduling =
                (merchantScope, operation, interval) -> {
                    PrototypeMerchantView merchant = this.merchantRuntime.merchant(
                            merchantScope.merchantIdentifier()
                    );
                    if (!merchant.capabilityIdentifiers().contains("scheduling")
                            || !operationConfigured(
                                    merchantScope.merchantIdentifier(),
                                    operation
                            )) {
                        throw new IllegalArgumentException(
                                "No active prototype scheduling configuration for operation"
                        );
                    }
                };

        AppointmentApplicationService appointment =
                new AppointmentApplicationService(
                        appointmentUnitOfWork,
                        customers,
                        scheduling,
                        prototypeAuthority,
                        platformClock
                );
        this.dispatcher = new ScopedOperationDispatcher<>(
                new ActiveOperationResolver(merchantRuntime.activation()),
                List.of(prototypeAuthority),
                appointment
        );
    }

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
        MerchantScope merchantScope = trustedMerchantScope(merchantIdentifier);
        String capacitySubject = capacitySubject(
                merchantScope.merchantIdentifier(),
                scheduledOperationIdentifier
        );
        ConfirmAppointmentCommand command = new ConfirmAppointmentCommand(
                merchantScope,
                commandIdentifier,
                appointmentIdentifier,
                customerContextIdentifier,
                scheduledOperationIdentifier,
                new TimeWindowAllocationScope(
                        capacitySubject,
                        startsAt,
                        endsAt
                )
        );
        dispatcher.dispatch(
                trustedContext(merchantScope),
                "appointment.confirm",
                command
        );
        return appointmentReader.apply(merchantScope, appointmentIdentifier)
                .orElseThrow(() -> new IllegalStateException(
                        "Confirmed prototype Appointment is not readable"
                ));
    }

    @Override
    public Optional<Appointment> appointment(
            String merchantIdentifier,
            String appointmentIdentifier
    ) {
        MerchantScope merchantScope = trustedMerchantScope(merchantIdentifier);
        return appointmentReader.apply(merchantScope, appointmentIdentifier);
    }

    private MerchantScope trustedMerchantScope(String merchantIdentifier) {
        PrototypeMerchantView merchant = merchantRuntime.merchant(merchantIdentifier);
        if (!merchant.operationIdentifiers().contains("appointment.confirm")) {
            throw new IllegalArgumentException(
                    "Appointment is not exposed for this prototype merchant"
            );
        }
        return new MerchantScope(merchant.merchantIdentifier());
    }

    private static String capacitySubject(
            String merchantIdentifier,
            String scheduledOperationIdentifier
    ) {
        return PrototypeAppointmentSubjectConfiguration.byScheduledOperation(
                        merchantIdentifier,
                        scheduledOperationIdentifier
                )
                .map(PrototypeAppointmentSubjectConfiguration.Subject::capacitySubjectReference)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown prototype scheduled operation for merchant: "
                                + scheduledOperationIdentifier
                ));
    }

    private static boolean operationConfigured(
            String merchantIdentifier,
            String scheduledOperationIdentifier
    ) {
        return PrototypeAppointmentSubjectConfiguration.byScheduledOperation(
                merchantIdentifier,
                scheduledOperationIdentifier
        ).isPresent();
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
