package mainstreet.runtime;

import mainstreet.application.MerchantScope;
import mainstreet.semantic.executable.ActiveOperationResolver;
import mainstreet.semantic.executable.ApplicableOperation;
import mainstreet.semantic.executable.ExecutableMerchantModel;
import mainstreet.semantic.executable.ExecutableObjectCreationEffect;
import mainstreet.semantic.executable.ExecutableOperationalObjectDefinition;
import mainstreet.semantic.executable.ExecutableOperationalObjectTypeIdentity;
import mainstreet.semantic.executable.ExecutableOperationDefinition;
import grandrue.testing.TestConfigurationReleases;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ScopedOperationDispatcherTest {

    private static final ExecutableOperationalObjectTypeIdentity BOOKING_TYPE =
            new ExecutableOperationalObjectTypeIdentity("booking", "booking");

    @Test
    void dispatches_one_guarded_model_snapshot_from_trusted_context_to_the_typed_handler() {
        Fixture fixture = fixture();
        ExecutionPrincipal principal = new ExecutionPrincipal("staff-1");
        AtomicReference<OperationExecutionContext> captured =
                new AtomicReference<>();
        CapabilityOperationHandler<CreateBooking> handler =
                (command, context) -> {
                    captured.set(context);
                    return OperationFulfilment.conformingTo(
                            context.applicableOperation(),
                            context.applicableOperation().operation().effects(),
                            Set.of("booking.created")
                    );
                };
        ScopedOperationDispatcher<CreateBooking> dispatcher =
                new ScopedOperationDispatcher<>(
                        fixture.resolver(),
                        List.of(guardAllowing("booking.create")),
                        handler
                );

        OperationFulfilment result = dispatcher.dispatch(
                trustedContext(fixture.scope(), principal),
                "booking.create",
                new CreateBooking("booking-1")
        );

        assertSame(principal, captured.get().principal());
        assertSame(fixture.scope(), captured.get().merchantScope());
        assertSame(fixture.model(), result.applicableOperation().model());
        assertSame(
                captured.get().applicableOperation(),
                result.applicableOperation()
        );
    }

    @Test
    void rejected_guard_never_invokes_the_capability_handler() {
        Fixture fixture = fixture();
        AtomicBoolean invoked = new AtomicBoolean();
        CapabilityOperationHandler<CreateBooking> handler =
                (command, context) -> {
                    invoked.set(true);
                    return OperationFulfilment.conformingTo(
                            context.applicableOperation(),
                            List.of(),
                            Set.of()
                    );
                };
        ScopedOperationDispatcher<CreateBooking> dispatcher =
                new ScopedOperationDispatcher<>(
                        fixture.resolver(),
                        List.of(new PrivilegeOperationExecutionGuard(
                                (scope, principal, privilege) -> false
                        )),
                        handler
                );

        assertThrows(
                AuthorizationException.class,
                () -> dispatcher.dispatch(
                        trustedContext(
                                fixture.scope(),
                                new ExecutionPrincipal("staff-1")
                        ),
                        "booking.create",
                        new CreateBooking("booking-1")
                )
        );
        assertFalse(invoked.get());
    }

    @Test
    void execution_context_rejects_a_model_from_another_scope() {
        Fixture fixture = fixture();
        ApplicableOperation operation = fixture.resolver().resolve(
                fixture.scope(),
                "booking.create"
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new OperationExecutionContext(
                        new MerchantScope("merchant-b"),
                        new ExecutionPrincipal("staff-1"),
                        operation
                )
        );
    }

    @Test
    void activation_during_guards_does_not_mix_model_snapshots() {
        Fixture fixture = fixture();
        ExecutableMerchantModel replacement = model(
                2,
                "booking.created.v2"
        );
        OperationExecutionGuard activationGuard = (scope, principal, operation) ->
                fixture.activation().activate(replacement);
        CapabilityOperationHandler<CreateBooking> handler =
                (command, context) -> OperationFulfilment.conformingTo(
                        context.applicableOperation(),
                        context.applicableOperation().operation().effects(),
                        Set.of("booking.created")
                );
        ScopedOperationDispatcher<CreateBooking> dispatcher =
                new ScopedOperationDispatcher<>(
                        fixture.resolver(),
                        List.of(
                                guardAllowing("booking.create"),
                                activationGuard
                        ),
                        handler
                );

        OperationFulfilment result = dispatcher.dispatch(
                trustedContext(
                        fixture.scope(),
                        new ExecutionPrincipal("staff-1")
                ),
                "booking.create",
                new CreateBooking("booking-1")
        );

        assertSame(fixture.model(), result.applicableOperation().model());
        assertSame(
                replacement,
                fixture.activation().current("merchant-a")
                        .orElseThrow().release().executableModel()
        );
        assertEquals(
                Set.of("booking.created"),
                result.committedEventIdentifiers()
        );
    }

    @Test
    void rejects_a_fulfilment_attributed_to_another_snapshot() {
        Fixture fixture = fixture();
        TestConfigurationReleases otherActivation = new TestConfigurationReleases();
        otherActivation.activate(model(1, "booking.created.foreign"));
        ApplicableOperation otherOperation = new ActiveOperationResolver(
                otherActivation.activation()
        ).resolve(fixture.scope(), "booking.create");
        CapabilityOperationHandler<CreateBooking> handler =
                (command, context) -> OperationFulfilment.conformingTo(
                        otherOperation,
                        otherOperation.operation().effects(),
                        Set.of("booking.created.foreign")
                );
        ScopedOperationDispatcher<CreateBooking> dispatcher =
                new ScopedOperationDispatcher<>(
                        fixture.resolver(),
                        List.of(guardAllowing("booking.create")),
                        handler
                );

        assertThrows(
                IllegalStateException.class,
                () -> dispatcher.dispatch(
                        trustedContext(
                                fixture.scope(),
                                new ExecutionPrincipal("staff-1")
                        ),
                        "booking.create",
                        new CreateBooking("booking-1")
                )
        );
    }

    @Test
    void rejects_dispatch_without_an_execution_guard() {
        Fixture fixture = fixture();

        assertThrows(
                IllegalArgumentException.class,
                () -> new ScopedOperationDispatcher<>(
                        fixture.resolver(),
                        List.of(),
                        (CreateBooking command, OperationExecutionContext context) ->
                                OperationFulfilment.conformingTo(
                                        context.applicableOperation(),
                                        List.of(),
                                        Set.of()
                                )
                )
        );
    }

    private static OperationExecutionGuard guardAllowing(
            String privilegeIdentifier
    ) {
        return new PrivilegeOperationExecutionGuard(
                (scope, principal, privilege) ->
                        scope.merchantIdentifier().equals("merchant-a")
                                && principal.identifier().equals("staff-1")
                                && privilege.identifier().equals(privilegeIdentifier)
        );
    }

    private static TrustedExecutionContext trustedContext(
            MerchantScope scope,
            ExecutionPrincipal principal
    ) {
        return new TrustedExecutionContext(scope, principal, Optional.empty());
    }

    private static Fixture fixture() {
        ExecutableMerchantModel model = model(1, "booking.created");
        TestConfigurationReleases activation = new TestConfigurationReleases();
        activation.activate(model);
        MerchantScope scope = new MerchantScope("merchant-a");
        return new Fixture(
                scope,
                model,
                activation,
                new ActiveOperationResolver(activation.activation())
        );
    }

    private static ExecutableMerchantModel model(
            long version,
            String eventIdentifier
    ) {
        ExecutableOperationDefinition create = new ExecutableOperationDefinition(
                "booking.create",
                List.of(new ExecutableObjectCreationEffect(
                        BOOKING_TYPE,
                        "requested"
                )),
                Set.of(eventIdentifier),
                "booking.create"
        );
        return new ExecutableMerchantModel(
                "merchant-a",
                "configuration-" + version,
                version,
                "semantic-registry-1.0",
                Set.of("booking"),
                List.of(new ExecutableOperationalObjectDefinition(
                        "booking",
                        "booking",
                        Set.of("requested"),
                        "requested"
                )),
                List.of(create)
        );
    }

    private record Fixture(
            MerchantScope scope,
            ExecutableMerchantModel model,
            TestConfigurationReleases activation,
            ActiveOperationResolver resolver
    ) {
    }

    private record CreateBooking(String bookingIdentifier) {
    }
}
