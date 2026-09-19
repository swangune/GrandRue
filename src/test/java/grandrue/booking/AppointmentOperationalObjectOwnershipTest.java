package grandrue.booking;

import grandrue.application.MerchantScope;
import grandrue.customer.CustomerContext;
import grandrue.customer.InMemoryCustomerContextAuthority;
import grandrue.runtime.ExecutionPrincipal;
import grandrue.runtime.ScopedOperationDispatcher;
import grandrue.runtime.TrustedExecutionContext;
import grandrue.semantic.TimeWindowAllocationScope;
import grandrue.semantic.executable.ActiveOperationResolver;
import grandrue.semantic.executable.ExecutableAllocationClaimEffect;
import grandrue.semantic.executable.ExecutableMerchantModel;
import grandrue.semantic.executable.ExecutableObjectCreationEffect;
import grandrue.semantic.executable.ExecutableOperationalObjectDefinition;
import grandrue.semantic.executable.ExecutableOperationalObjectTypeIdentity;
import grandrue.semantic.executable.ExecutableOperationDefinition;
import grandrue.semantic.executable.ExecutableOperationEffect;
import grandrue.semantic.executable.ExecutableRelationshipDefinition;
import grandrue.semantic.executable.ExecutableRelationshipEstablishmentEffect;
import grandrue.semantic.registry.RelationshipCardinality;
import grandrue.semantic.registry.RelationshipScopeConstraint;
import grandrue.scheduling.AppointmentApplicationService;
import grandrue.scheduling.AppointmentCommandIdentityConflictException;
import grandrue.scheduling.ConfirmAppointmentCommand;
import grandrue.scheduling.InMemoryAppointmentUnitOfWork;
import grandrue.testing.TestConfigurationReleases;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AppointmentOperationalObjectOwnershipTest {

    private static final MerchantScope MERCHANT_SCOPE =
            new MerchantScope("merchant-a");
    private static final ExecutableOperationalObjectTypeIdentity APPOINTMENT_TYPE =
            new ExecutableOperationalObjectTypeIdentity("appointment", "appointment");
    private static final ExecutableOperationalObjectTypeIdentity CUSTOMER_CONTEXT_TYPE =
            new ExecutableOperationalObjectTypeIdentity(
                    "customer",
                    "customer-context"
            );

    @Test
    void appointment_confirmation_preserves_appointment_owned_object_identity() {
        ExecutableMerchantModel model = model();
        assertTrue(model.operationalObject("appointment", "appointment").isPresent());
        assertTrue(model.operationalObject("booking", "appointment").isEmpty());

        InMemoryAppointmentUnitOfWork unitOfWork =
                new InMemoryAppointmentUnitOfWork();
        InMemoryCustomerContextAuthority customerContexts =
                new InMemoryCustomerContextAuthority();
        customerContexts.register(new CustomerContext(
                MERCHANT_SCOPE,
                "customer-123",
                Instant.parse("2026-08-20T10:16:00Z")
        ));

        AppointmentApplicationService handler = new AppointmentApplicationService(
                unitOfWork,
                customerContexts,
                (scope, operation, interval) -> { },
                (scope, principal, operation) -> { },
                java.time.Clock.systemUTC()
        );
        TestConfigurationReleases releases = new TestConfigurationReleases();
        releases.activate(model);
        ScopedOperationDispatcher<ConfirmAppointmentCommand> dispatcher =
                new ScopedOperationDispatcher<>(
                        new ActiveOperationResolver(releases.activation()),
                        List.of((scope, principal, operation) -> { }),
                        handler
                );

        var fulfilment = dispatcher.dispatch(
                new TrustedExecutionContext(
                        MERCHANT_SCOPE,
                        new ExecutionPrincipal("staff-1"),
                        Optional.empty()
                ),
                "appointment.confirm",
                command()
        );

        assertEquals(modelEffects(), fulfilment.effects());
        assertTrue(unitOfWork.appointment(
                MERCHANT_SCOPE,
                "appointment-123"
        ).isPresent());
    }

    @Test
    void appointment_command_identifier_cannot_be_reused_for_different_intent() {
        ExecutableMerchantModel model = model();

        InMemoryAppointmentUnitOfWork unitOfWork =
                new InMemoryAppointmentUnitOfWork();
        InMemoryCustomerContextAuthority customerContexts =
                new InMemoryCustomerContextAuthority();
        customerContexts.register(new CustomerContext(
                MERCHANT_SCOPE,
                "customer-123",
                Instant.parse("2026-08-20T10:16:00Z")
        ));

        AppointmentApplicationService handler = new AppointmentApplicationService(
                unitOfWork,
                customerContexts,
                (scope, operation, interval) -> { },
                (scope, principal, operation) -> { },
                java.time.Clock.systemUTC()
        );
        TestConfigurationReleases releases = new TestConfigurationReleases();
        releases.activate(model);
        ScopedOperationDispatcher<ConfirmAppointmentCommand> dispatcher =
                new ScopedOperationDispatcher<>(
                        new ActiveOperationResolver(releases.activation()),
                        List.of((scope, principal, operation) -> { }),
                        handler
                );

        TrustedExecutionContext executionContext =
                new TrustedExecutionContext(
                        MERCHANT_SCOPE,
                        new ExecutionPrincipal("staff-1"),
                        Optional.empty()
                );

        dispatcher.dispatch(
                executionContext,
                "appointment.confirm",
                command()
        );

        ConfirmAppointmentCommand conflictingCommand =
                new ConfirmAppointmentCommand(
                        MERCHANT_SCOPE,
                        "command-001",
                        "appointment-123",
                        "customer-123",
                        "consultation.perform",
                        new TimeWindowAllocationScope(
                                "consultant-capacity-1",
                                Instant.parse("2026-08-20T15:00:00Z"),
                                Instant.parse("2026-08-20T15:30:00Z")
                        )
                );

        assertThrows(
                AppointmentCommandIdentityConflictException.class,
                () -> dispatcher.dispatch(
                        executionContext,
                        "appointment.confirm",
                        conflictingCommand
                )
        );
    }

    private static ExecutableMerchantModel model() {
        return new ExecutableMerchantModel(
                MERCHANT_SCOPE.merchantIdentifier(),
                "configuration-1",
                1,
                "semantic-registry-1.0",
                Set.of("appointment", "scheduling", "customer"),
                List.of(
                        new ExecutableOperationalObjectDefinition(
                                "appointment",
                                "appointment",
                                Set.of("confirmed"),
                                Optional.of("confirmed"),
                                Optional.empty()
                        ),
                        new ExecutableOperationalObjectDefinition(
                                "customer",
                                "customer-context",
                                Set.of(),
                                Optional.empty(),
                                Optional.empty()
                        )
                ),
                List.of(),
                List.of(new ExecutableRelationshipDefinition(
                        "appointment.customer-context",
                        "CUSTOMER",
                        APPOINTMENT_TYPE,
                        CUSTOMER_CONTEXT_TYPE,
                        RelationshipCardinality.REQUIRED_ONE,
                        RelationshipScopeConstraint.SAME_MERCHANT
                )),
                List.of(new ExecutableOperationDefinition(
                        "appointment.confirm",
                        modelEffects(),
                        Set.of("appointment.confirmed"),
                        "appointment.confirm"
                )),
                List.of()
        );
    }

    private static List<ExecutableOperationEffect> modelEffects() {
        return List.of(
                new ExecutableObjectCreationEffect(APPOINTMENT_TYPE, "confirmed"),
                new ExecutableRelationshipEstablishmentEffect(
                        "appointment.customer-context"
                ),
                new ExecutableAllocationClaimEffect("appointment.capacity")
        );
    }

    private static ConfirmAppointmentCommand command() {
        return new ConfirmAppointmentCommand(
                MERCHANT_SCOPE,
                "command-001",
                "appointment-123",
                "customer-123",
                "consultation.perform",
                new TimeWindowAllocationScope(
                        "consultant-capacity-1",
                        Instant.parse("2026-08-20T14:00:00Z"),
                        Instant.parse("2026-08-20T14:30:00Z")
                )
        );
    }
}