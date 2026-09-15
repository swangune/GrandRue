package mainstreet.prototype;

import mainstreet.application.MerchantScope;
import mainstreet.customer.CustomerContext;
import mainstreet.customer.InMemoryCustomerContextAuthority;
import mainstreet.money.CurrencyIdentity;
import mainstreet.money.MonetaryAmount;
import mainstreet.ordering.CommitOrderCommand;
import mainstreet.ordering.CommittedQuantity;
import mainstreet.ordering.InMemoryOrderingUnitOfWork;
import mainstreet.ordering.OrderCommitmentPortion;
import mainstreet.ordering.OrderInventoryClaimRequest;
import mainstreet.ordering.OrderingApplicationService;
import mainstreet.ordering.RequestedOrderPortion;
import mainstreet.ordering.ResolvedOrderCommitment;
import mainstreet.runtime.ExecutionPrincipal;
import mainstreet.runtime.OperationExecutionGuard;
import mainstreet.runtime.ScopedOperationDispatcher;
import mainstreet.runtime.TrustedExecutionContext;
import mainstreet.scheduling.AppointmentApplicationService;
import mainstreet.scheduling.ConfirmAppointmentCommand;
import mainstreet.scheduling.InMemoryAppointmentUnitOfWork;
import mainstreet.semantic.QuantityAllocationScope;
import mainstreet.semantic.TimeWindowAllocationScope;
import mainstreet.semantic.executable.ActiveOperationResolver;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PrototypeRuntimeDispatchTest {

    private static final Instant RECORDED_AT =
            Instant.parse("2026-08-26T03:00:00Z");

    @Test
    void retailer_configuration_drives_ordering_execution_through_runtime_dispatch() {
        PrototypeMerchantRuntime runtime = PrototypeMerchantRuntime.standard();
        MerchantScope merchant = new MerchantScope("prototype-retailer");
        InMemoryOrderingUnitOfWork unitOfWork =
                new InMemoryOrderingUnitOfWork(Map.of("sku-1", 1L));
        OrderingApplicationService ordering = new OrderingApplicationService(
                unitOfWork,
                new InMemoryCustomerContextAuthority(),
                (scope, requested) -> new ResolvedOrderCommitment(
                        new OrderCommitmentPortion(
                                requested.identifier(),
                                requested.subjectReference(),
                                requested.quantity(),
                                new MonetaryAmount(
                                        new CurrencyIdentity("GBP"),
                                        BigInteger.valueOf(2500)
                                ),
                                "prototype-offering:sku-1@1"
                        ),
                        List.of(new OrderInventoryClaimRequest(
                                "stock",
                                requested.identifier(),
                                new QuantityAllocationScope(
                                        requested.subjectReference(),
                                        requested.quantity().magnitude()
                                                .longValueExact()
                                )
                        ))
                ),
                OperationExecutionGuard.allowAll(),
                Clock.fixed(RECORDED_AT, ZoneOffset.UTC)
        );
        ScopedOperationDispatcher<CommitOrderCommand> dispatcher =
                new ScopedOperationDispatcher<>(
                        new ActiveOperationResolver(runtime.activation()),
                        List.of(OperationExecutionGuard.allowAll()),
                        ordering
                );
        CommitOrderCommand command = new CommitOrderCommand(
                merchant,
                "prototype-order-command-1",
                "prototype-order-1",
                Optional.empty(),
                List.of(new RequestedOrderPortion(
                        "portion-1",
                        "sku-1",
                        new CommittedQuantity(BigDecimal.ONE, "EACH")
                ))
        );

        var fulfilment = dispatcher.dispatch(
                trustedContext(merchant),
                "ordering.commit",
                command
        );

        assertEquals("prototype-retailer-release-1",
                fulfilment.applicableOperation().releaseIdentifier());
        assertTrue(unitOfWork.order(merchant, "prototype-order-1").isPresent());
        assertEquals(0L, unitOfWork.availableToPromise("sku-1"));
    }

    @Test
    void consultant_configuration_drives_appointment_execution_through_runtime_dispatch() {
        PrototypeMerchantRuntime runtime = PrototypeMerchantRuntime.standard();
        MerchantScope merchant = new MerchantScope("prototype-consultant");
        InMemoryAppointmentUnitOfWork unitOfWork =
                new InMemoryAppointmentUnitOfWork();
        InMemoryCustomerContextAuthority customers =
                new InMemoryCustomerContextAuthority();
        customers.register(new CustomerContext(
                merchant,
                "customer-1",
                RECORDED_AT.minusSeconds(60)
        ));
        AppointmentApplicationService appointment =
                new AppointmentApplicationService(
                        unitOfWork,
                        customers,
                        (scope, operation, interval) -> {
                            // Scheduling revalidation succeeds for this dispatch test.
                        },
                        OperationExecutionGuard.allowAll(),
                        Clock.fixed(RECORDED_AT, ZoneOffset.UTC)
                );
        ScopedOperationDispatcher<ConfirmAppointmentCommand> dispatcher =
                new ScopedOperationDispatcher<>(
                        new ActiveOperationResolver(runtime.activation()),
                        List.of(OperationExecutionGuard.allowAll()),
                        appointment
                );
        ConfirmAppointmentCommand command = new ConfirmAppointmentCommand(
                merchant,
                "prototype-appointment-command-1",
                "prototype-appointment-1",
                "customer-1",
                "consultation.perform",
                new TimeWindowAllocationScope(
                        "consultant-1",
                        Instant.parse("2026-08-27T09:00:00Z"),
                        Instant.parse("2026-08-27T10:00:00Z")
                )
        );

        var fulfilment = dispatcher.dispatch(
                trustedContext(merchant),
                "appointment.confirm",
                command
        );

        assertEquals("prototype-consultant-release-1",
                fulfilment.applicableOperation().releaseIdentifier());
        assertTrue(unitOfWork.appointment(
                merchant,
                "prototype-appointment-1"
        ).isPresent());
    }

    @Test
    void publisher_configuration_cannot_execute_unselected_ordering_operation() {
        PrototypeMerchantRuntime runtime = PrototypeMerchantRuntime.standard();
        MerchantScope merchant = new MerchantScope("prototype-publisher");
        InMemoryOrderingUnitOfWork unitOfWork =
                new InMemoryOrderingUnitOfWork(Map.of("sku-1", 1L));
        OrderingApplicationService ordering = new OrderingApplicationService(
                unitOfWork,
                new InMemoryCustomerContextAuthority(),
                (scope, requested) -> {
                    throw new AssertionError("Ordering revalidation must not run");
                },
                OperationExecutionGuard.allowAll(),
                Clock.fixed(RECORDED_AT, ZoneOffset.UTC)
        );
        ScopedOperationDispatcher<CommitOrderCommand> dispatcher =
                new ScopedOperationDispatcher<>(
                        new ActiveOperationResolver(runtime.activation()),
                        List.of(OperationExecutionGuard.allowAll()),
                        ordering
                );
        CommitOrderCommand command = new CommitOrderCommand(
                merchant,
                "command-1",
                "order-1",
                Optional.empty(),
                List.of(new RequestedOrderPortion(
                        "portion-1",
                        "sku-1",
                        new CommittedQuantity(BigDecimal.ONE, "EACH")
                ))
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> dispatcher.dispatch(
                        trustedContext(merchant),
                        "ordering.commit",
                        command
                )
        );
        assertTrue(unitOfWork.order(merchant, "order-1").isEmpty());
    }

    private static TrustedExecutionContext trustedContext(
            MerchantScope merchantScope
    ) {
        return new TrustedExecutionContext(
                merchantScope,
                new ExecutionPrincipal(PrototypeMerchantRuntime.PROTOTYPE_PRINCIPAL),
                Optional.empty()
        );
    }
}
