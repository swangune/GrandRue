package mainstreet.booking;

import mainstreet.application.MerchantScope;
import mainstreet.customer.CustomerContext;
import mainstreet.customer.InMemoryCustomerContextAuthority;
import mainstreet.runtime.ExecutionPrincipal;
import mainstreet.runtime.ScopedOperationDispatcher;
import mainstreet.runtime.TrustedExecutionContext;
import mainstreet.semantic.TimeWindowAllocationScope;
import mainstreet.semantic.executable.ActiveOperationResolver;
import mainstreet.semantic.executable.ExecutableAllocationClaimEffect;
import mainstreet.semantic.executable.ExecutableMerchantModel;
import mainstreet.semantic.executable.ExecutableObjectCreationEffect;
import mainstreet.semantic.executable.ExecutableOperationalObjectDefinition;
import mainstreet.semantic.executable.ExecutableOperationalObjectTypeIdentity;
import mainstreet.semantic.executable.ExecutableOperationDefinition;
import mainstreet.semantic.executable.ExecutableOperationEffect;
import mainstreet.semantic.executable.ExecutableRelationshipDefinition;
import mainstreet.semantic.executable.ExecutableRelationshipEstablishmentEffect;
import mainstreet.semantic.registry.RelationshipCardinality;
import mainstreet.semantic.registry.RelationshipScopeConstraint;
import mainstreet.scheduling.AppointmentApplicationService;
import mainstreet.scheduling.ConfirmAppointmentCommand;
import mainstreet.scheduling.InMemoryAppointmentUnitOfWork;
import mainstreet.testing.TestConfigurationReleases;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
