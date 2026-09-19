package grandrue.runtime;

import grandrue.application.MerchantScope;
import grandrue.semantic.executable.ActiveOperationResolver;
import grandrue.semantic.executable.ApplicableOperation;
import grandrue.semantic.executable.ExecutableMerchantModel;
import grandrue.semantic.executable.ExecutableObjectCreationEffect;
import grandrue.semantic.executable.ExecutableOperationalObjectDefinition;
import grandrue.semantic.executable.ExecutableOperationalObjectTypeIdentity;
import grandrue.semantic.executable.ExecutableOperationDefinition;
import grandrue.semantic.executable.ExecutableOperationEffect;
import grandrue.semantic.executable.ExecutableStateTransitionEffect;
import grandrue.testing.TestConfigurationReleases;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CapabilityOperationHandlerTest {

    private static final ExecutableOperationalObjectTypeIdentity BOOKING_TYPE =
            new ExecutableOperationalObjectTypeIdentity("booking", "booking");

    @Test
    void typed_capability_handler_reports_a_conforming_fulfilment() {
        ApplicableOperation operation = applicableOperation();
        CreateBooking command = new CreateBooking("booking-1");
        CapabilityOperationHandler<CreateBooking> handler =
                (candidate, context) -> OperationFulfilment.conformingTo(
                        context.applicableOperation(),
                        context.applicableOperation().operation().effects(),
                        Set.of("booking.created")
                );

        OperationFulfilment fulfilment = handler.fulfill(
                command,
                new OperationExecutionContext(
                        new MerchantScope("merchant-a"),
                        new ExecutionPrincipal("staff-1"),
                        operation
                )
        );

        assertSame(operation, fulfilment.applicableOperation());
        assertEquals(operation.operation().effects(), fulfilment.effects());
        assertEquals(
                Set.of("booking.created"),
                fulfilment.committedEventIdentifiers()
        );
    }

    @Test
    void rejects_an_effect_not_declared_by_the_executable_operation() {
        ApplicableOperation operation = applicableOperation();
        ExecutableOperationEffect undeclared = new ExecutableStateTransitionEffect(
                BOOKING_TYPE,
                "requested",
                "confirmed"
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> OperationFulfilment.conformingTo(
                        operation,
                        List.of(undeclared),
                        Set.of()
                )
        );
    }

    @Test
    void rejects_a_committed_event_not_declared_by_the_operation() {
        ApplicableOperation operation = applicableOperation();

        assertThrows(
                IllegalArgumentException.class,
                () -> OperationFulfilment.conformingTo(
                        operation,
                        operation.operation().effects(),
                        Set.of("payment.captured")
                )
        );
    }

    @Test
    void rejects_more_effect_occurrences_than_the_operation_declares() {
        ApplicableOperation operation = applicableOperation();
        ExecutableOperationEffect declared = operation.operation()
                .effects().getFirst();

        assertThrows(
                IllegalArgumentException.class,
                () -> OperationFulfilment.conformingTo(
                        operation,
                        List.of(declared, declared),
                        Set.of()
                )
        );
    }

    @Test
    void defensively_copies_reported_effects_and_events() {
        ApplicableOperation operation = applicableOperation();
        List<ExecutableOperationEffect> effects = new ArrayList<>(
                operation.operation().effects()
        );
        Set<String> events = new HashSet<>(Set.of("booking.created"));

        OperationFulfilment fulfilment = OperationFulfilment.conformingTo(
                operation,
                effects,
                events
        );
        effects.clear();
        events.clear();

        assertEquals(operation.operation().effects(), fulfilment.effects());
        assertEquals(
                Set.of("booking.created"),
                fulfilment.committedEventIdentifiers()
        );
    }

    private static ApplicableOperation applicableOperation() {
        ExecutableOperationDefinition create = new ExecutableOperationDefinition(
                "booking.create",
                List.of(new ExecutableObjectCreationEffect(
                        BOOKING_TYPE,
                        "requested"
                )),
                Set.of("booking.created"),
                "booking.create"
        );
        ExecutableMerchantModel model = new ExecutableMerchantModel(
                "merchant-a",
                "configuration-1",
                1,
                "semantic-registry-1.0",
                Set.of("booking"),
                List.of(new ExecutableOperationalObjectDefinition(
                        "booking",
                        "booking",
                        Set.of("requested", "confirmed"),
                        "requested"
                )),
                List.of(create)
        );
        TestConfigurationReleases releases = new TestConfigurationReleases();
        releases.activate(model);
        return new ActiveOperationResolver(releases.activation()).resolve(
                new MerchantScope("merchant-a"),
                "booking.create"
        );
    }

    private record CreateBooking(String bookingIdentifier) {
    }
}
