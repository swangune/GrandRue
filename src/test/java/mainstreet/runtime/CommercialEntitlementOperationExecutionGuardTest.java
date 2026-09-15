package mainstreet.runtime;

import mainstreet.application.MerchantScope;
import mainstreet.commercial.CommercialEntitlementAuthority;
import mainstreet.commercial.CommercialEntitlementException;
import mainstreet.commercial.CommercialEntitlementIdentity;
import mainstreet.semantic.executable.ActiveOperationResolver;
import mainstreet.semantic.executable.ApplicableOperation;
import mainstreet.semantic.executable.ExecutableMerchantModel;
import mainstreet.semantic.executable.ExecutableObjectCreationEffect;
import mainstreet.semantic.executable.ExecutableOperationalObjectDefinition;
import mainstreet.semantic.executable.ExecutableOperationalObjectTypeIdentity;
import mainstreet.semantic.executable.ExecutableOperationDefinition;
import mainstreet.testing.TestConfigurationReleases;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommercialEntitlementOperationExecutionGuardTest {

    private static final ExecutableOperationalObjectTypeIdentity BOOKING_TYPE =
            new ExecutableOperationalObjectTypeIdentity("booking", "booking");
    private static final CommercialEntitlementIdentity BOOKING_NEW_ACTIVITY =
            new CommercialEntitlementIdentity("booking.new-activity");

    @Test
    void denied_entitlement_blocks_dispatch_without_removing_semantic_applicability() {
        Fixture fixture = fixture();
        AtomicBoolean handlerInvoked = new AtomicBoolean();
        AtomicReference<MerchantScope> checkedScope = new AtomicReference<>();
        CommercialEntitlementAuthority entitlementAuthority = (scope, entitlement) -> {
            checkedScope.set(scope);
            assertEquals(BOOKING_NEW_ACTIVITY, entitlement);
            return false;
        };
        ScopedOperationDispatcher<CreateBooking> dispatcher = new ScopedOperationDispatcher<>(
                fixture.resolver(),
                List.of(
                        actorGuard(),
                        new CommercialEntitlementOperationExecutionGuard(
                                entitlementAuthority,
                                Map.of("booking.create", BOOKING_NEW_ACTIVITY)
                        )
                ),
                (command, context) -> {
                    handlerInvoked.set(true);
                    return OperationFulfilment.conformingTo(
                            context.applicableOperation(),
                            context.applicableOperation().operation().effects(),
                            Set.of("booking.created")
                    );
                }
        );

        assertThrows(
                CommercialEntitlementException.class,
                () -> dispatcher.dispatch(
                        trustedContext(fixture),
                        "booking.create",
                        new CreateBooking("booking-1")
                )
        );

        assertFalse(handlerInvoked.get());
        assertEquals(fixture.scope(), checkedScope.get());
        ApplicableOperation stillApplicable = fixture.resolver().resolve(
                fixture.scope(),
                "booking.create"
        );
        assertSame(fixture.model(), stillApplicable.model());
        assertTrue(stillApplicable.model().capabilityIdentifiers().contains("booking"));
    }

    @Test
    void entitled_applicable_operation_reaches_capability_owned_handler() {
        Fixture fixture = fixture();
        AtomicBoolean handlerInvoked = new AtomicBoolean();
        CommercialEntitlementAuthority entitlementAuthority = (scope, entitlement) ->
                scope.equals(fixture.scope()) && entitlement.equals(BOOKING_NEW_ACTIVITY);
        ScopedOperationDispatcher<CreateBooking> dispatcher = new ScopedOperationDispatcher<>(
                fixture.resolver(),
                List.of(
                        actorGuard(),
                        new CommercialEntitlementOperationExecutionGuard(
                                entitlementAuthority,
                                Map.of("booking.create", BOOKING_NEW_ACTIVITY)
                        )
                ),
                (command, context) -> {
                    handlerInvoked.set(true);
                    return OperationFulfilment.conformingTo(
                            context.applicableOperation(),
                            context.applicableOperation().operation().effects(),
                            Set.of("booking.created")
                    );
                }
        );

        OperationFulfilment fulfilment = dispatcher.dispatch(
                trustedContext(fixture),
                "booking.create",
                new CreateBooking("booking-1")
        );

        assertTrue(handlerInvoked.get());
        assertSame(fixture.model(), fulfilment.applicableOperation().model());
    }

    @Test
    void operation_without_commercial_gate_does_not_fabricate_entitlement_check() {
        Fixture fixture = fixture();
        CommercialEntitlementAuthority entitlementAuthority = (scope, entitlement) -> {
            throw new AssertionError("No entitlement check should be evaluated");
        };
        ScopedOperationDispatcher<CreateBooking> dispatcher = new ScopedOperationDispatcher<>(
                fixture.resolver(),
                List.of(
                        actorGuard(),
                        new CommercialEntitlementOperationExecutionGuard(
                                entitlementAuthority,
                                Map.of()
                        )
                ),
                (command, context) -> OperationFulfilment.conformingTo(
                        context.applicableOperation(),
                        context.applicableOperation().operation().effects(),
                        Set.of("booking.created")
                )
        );

        OperationFulfilment fulfilment = dispatcher.dispatch(
                trustedContext(fixture),
                "booking.create",
                new CreateBooking("booking-1")
        );

        assertSame(fixture.model(), fulfilment.applicableOperation().model());
    }

    @Test
    void commercial_entitlement_identity_rejects_blank_identifier() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new CommercialEntitlementIdentity("  ")
        );
    }

    private static OperationExecutionGuard actorGuard() {
        return new PrivilegeOperationExecutionGuard(
                (scope, principal, privilege) ->
                        scope.merchantIdentifier().equals("merchant-a")
                                && principal.identifier().equals("staff-1")
                                && privilege.identifier().equals("booking.create")
        );
    }

    private static ExecutionPrincipal authorisedPrincipal() {
        return new ExecutionPrincipal("staff-1");
    }

    private static TrustedExecutionContext trustedContext(Fixture fixture) {
        return new TrustedExecutionContext(
                fixture.scope(),
                authorisedPrincipal(),
                Optional.empty()
        );
    }

    private static Fixture fixture() {
        ExecutableMerchantModel model = model();
        TestConfigurationReleases releases = new TestConfigurationReleases();
        releases.activate(model);
        MerchantScope scope = new MerchantScope("merchant-a");
        return new Fixture(
                scope,
                model,
                new ActiveOperationResolver(releases.activation())
        );
    }

    private static ExecutableMerchantModel model() {
        ExecutableOperationDefinition create = new ExecutableOperationDefinition(
                "booking.create",
                List.of(new ExecutableObjectCreationEffect(
                        BOOKING_TYPE,
                        "requested"
                )),
                Set.of("booking.created"),
                "booking.create"
        );
        return new ExecutableMerchantModel(
                "merchant-a",
                "configuration-1",
                1,
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
            ActiveOperationResolver resolver
    ) {
    }

    private record CreateBooking(String bookingIdentifier) {
    }
}
