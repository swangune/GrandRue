package mainstreet.booking;

import mainstreet.application.MerchantScope;
import mainstreet.customer.CustomerContext;
import mainstreet.customer.InMemoryCustomerContextAuthority;
import mainstreet.runtime.AuthorizationException;
import mainstreet.runtime.ExecutionPrincipal;
import mainstreet.runtime.OperationExecutionGuard;
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
import grandrue.testing.TestConfigurationReleases;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookingOperationHandlerTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");
    private static final Instant START = Instant.parse("2026-08-28T14:00:00Z");
    private static final Instant END = Instant.parse("2026-08-30T10:00:00Z");
    private static final ExecutableOperationalObjectTypeIdentity BOOKING =
            new ExecutableOperationalObjectTypeIdentity("booking", "booking");
    private static final ExecutableOperationalObjectTypeIdentity CUSTOMER =
            new ExecutableOperationalObjectTypeIdentity("customer", "customer-context");

    @Test
    void scoped_dispatch_fulfils_booking_only_effect_set() {
        InMemoryBookingUnitOfWork unitOfWork = new InMemoryBookingUnitOfWork();
        ScopedOperationDispatcher<ConfirmBookingCommand> dispatcher =
                dispatcher(model("booking.confirm", modelEffects()), unitOfWork,
                        authorisationGuard());
        ConfirmBookingCommand command = command(MERCHANT);

        var fulfilment = dispatcher.dispatch(
                trustedContext(),
                "booking.confirm",
                command
        );

        assertEquals(modelEffects(), fulfilment.effects());
        assertEquals(Set.of("booking.confirmed"), fulfilment.committedEventIdentifiers());
        Booking booking = unitOfWork.booking(MERCHANT, "booking-1").orElseThrow();
        assertEquals("standard-room", booking.bookedSubjectReference());
        assertEquals(new BookingReservationWindow(START, END), booking.reservationWindow());
        assertTrue(unitOfWork.conflictingClaim(MERCHANT, command.allocationScope())
                .isPresent());
    }

    @Test
    void incomplete_registered_effect_set_is_rejected_before_mutation() {
        InMemoryBookingUnitOfWork unitOfWork = new InMemoryBookingUnitOfWork();
        ScopedOperationDispatcher<ConfirmBookingCommand> dispatcher = dispatcher(
                model("booking.confirm", List.of(modelEffects().getFirst())),
                unitOfWork,
                authorisationGuard()
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> dispatcher.dispatch(
                        trustedContext(),
                        "booking.confirm",
                        command(MERCHANT)
                )
        );
        assertTrue(unitOfWork.booking(MERCHANT, "booking-1").isEmpty());
    }

    @Test
    void trusted_merchant_scope_must_match_command_scope() {
        InMemoryBookingUnitOfWork unitOfWork = new InMemoryBookingUnitOfWork();
        ScopedOperationDispatcher<ConfirmBookingCommand> dispatcher =
                dispatcher(model("booking.confirm", modelEffects()), unitOfWork,
                        authorisationGuard());

        assertThrows(
                IllegalArgumentException.class,
                () -> dispatcher.dispatch(
                        trustedContext(),
                        "booking.confirm",
                        command(new MerchantScope("merchant-b"))
                )
        );
        assertTrue(unitOfWork.booking(
                new MerchantScope("merchant-b"),
                "booking-1"
        ).isEmpty());
    }

    @Test
    void final_actor_authority_is_revalidated_before_mutation() {
        InMemoryBookingUnitOfWork unitOfWork = new InMemoryBookingUnitOfWork();
        OperationExecutionGuard revoked = (scope, principal, operation) -> {
            throw new AuthorizationException("Authority revoked");
        };
        ScopedOperationDispatcher<ConfirmBookingCommand> dispatcher =
                dispatcher(model("booking.confirm", modelEffects()), unitOfWork, revoked);

        assertThrows(
                AuthorizationException.class,
                () -> dispatcher.dispatch(
                        trustedContext(),
                        "booking.confirm",
                        command(MERCHANT)
                )
        );
        assertTrue(unitOfWork.booking(MERCHANT, "booking-1").isEmpty());
    }

    private static ScopedOperationDispatcher<ConfirmBookingCommand> dispatcher(
            ExecutableMerchantModel model,
            InMemoryBookingUnitOfWork unitOfWork,
            OperationExecutionGuard guard
    ) {
        TestConfigurationReleases releases = new TestConfigurationReleases();
        releases.activate(model);
        BookingApplicationService handler = new BookingApplicationService(
                unitOfWork,
                customerContexts(),
                guard
        );
        return new ScopedOperationDispatcher<>(
                new ActiveOperationResolver(releases.activation()),
                List.of((scope, principal, operation) -> { }),
                handler
        );
    }

    private static ExecutableMerchantModel model(
            String operationIdentifier,
            List<ExecutableOperationEffect> effects
    ) {
        return new ExecutableMerchantModel(
                MERCHANT.merchantIdentifier(),
                "configuration-1",
                1,
                "semantic-registry-1.0",
                Set.of("booking", "customer"),
                List.of(
                        new ExecutableOperationalObjectDefinition(
                                "booking", "booking", Set.of("confirmed"),
                                Optional.of("confirmed"), Optional.empty()
                        ),
                        new ExecutableOperationalObjectDefinition(
                                "customer", "customer-context", Set.of(),
                                Optional.empty(), Optional.empty()
                        )
                ),
                List.of(),
                List.of(new ExecutableRelationshipDefinition(
                        "booking.customer-context",
                        "CUSTOMER",
                        BOOKING,
                        CUSTOMER,
                        RelationshipCardinality.REQUIRED_ONE,
                        RelationshipScopeConstraint.SAME_MERCHANT
                )),
                List.of(new ExecutableOperationDefinition(
                        operationIdentifier,
                        effects,
                        Set.of("booking.confirmed"),
                        operationIdentifier
                )),
                List.of()
        );
    }

    private static List<ExecutableOperationEffect> modelEffects() {
        return List.of(
                new ExecutableObjectCreationEffect(BOOKING, "confirmed"),
                new ExecutableRelationshipEstablishmentEffect(
                        "booking.customer-context"
                ),
                new ExecutableAllocationClaimEffect("booking.capacity")
        );
    }

    private static ConfirmBookingCommand command(MerchantScope scope) {
        return new ConfirmBookingCommand(
                scope,
                "command-1",
                "booking-1",
                "customer-1",
                "standard-room",
                new BookingReservationWindow(START, END),
                new TimeWindowAllocationScope("capacity-1", START, END)
        );
    }

    private static InMemoryCustomerContextAuthority customerContexts() {
        InMemoryCustomerContextAuthority customers =
                new InMemoryCustomerContextAuthority();
        customers.register(new CustomerContext(MERCHANT, "customer-1", Instant.EPOCH));
        return customers;
    }

    private static OperationExecutionGuard authorisationGuard() {
        return (scope, principal, operation) -> {
            if (!MERCHANT.equals(scope) || !"staff-1".equals(principal.identifier())) {
                throw new AuthorizationException("Not authorised");
            }
        };
    }

    private static TrustedExecutionContext trustedContext() {
        return new TrustedExecutionContext(
                MERCHANT,
                new ExecutionPrincipal("staff-1"),
                Optional.empty()
        );
    }
}
