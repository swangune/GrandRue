package mainstreet.prototype;

import grandrue.application.MerchantScope;
import grandrue.customer.InMemoryCustomerContextAuthority;
import grandrue.money.CurrencyIdentity;
import grandrue.money.MonetaryAmount;
import grandrue.ordering.CommitOrderCommand;
import grandrue.ordering.Order;
import grandrue.ordering.OrderCommitmentPortion;
import grandrue.ordering.OrderInventoryClaimRequest;
import grandrue.ordering.OrderingApplicationService;
import grandrue.ordering.OrderingUnitOfWork;
import grandrue.ordering.RequestedOrderPortion;
import grandrue.ordering.ResolvedOrderCommitment;
import mainstreet.runtime.AuthorizationException;
import mainstreet.runtime.ExecutionPrincipal;
import mainstreet.runtime.OperationExecutionGuard;
import mainstreet.runtime.ScopedOperationDispatcher;
import mainstreet.runtime.TrustedExecutionContext;
import mainstreet.semantic.QuantityAllocationScope;
import grandrue.semantic.executable.ActiveOperationResolver;

import java.math.BigInteger;
import java.time.Clock;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * Durable pre-UI Ordering composition for the reference retailer.
 *
 * <p>The proposition and stock setup are deterministic prototype fixtures. They
 * stand in for already-designed Offering/Inventory authorities and must not be
 * promoted as production commerce configuration.</p>
 */
public final class PrototypeJooqOrderingUseCase
        implements PrototypeOrderUseCase {

    private final PrototypeMerchantRuntime merchantRuntime;
    private final BiFunction<MerchantScope, String, Optional<Order>> orderReader;
    private final ScopedOperationDispatcher<CommitOrderCommand> dispatcher;

    public PrototypeJooqOrderingUseCase(
            PrototypeMerchantRuntime merchantRuntime,
            OrderingUnitOfWork unitOfWork,
            BiFunction<MerchantScope, String, Optional<Order>> orderReader,
            Clock clock
    ) {
        this.merchantRuntime = Objects.requireNonNull(
                merchantRuntime,
                "merchantRuntime"
        );
        OrderingUnitOfWork orderingUnitOfWork = Objects.requireNonNull(
                unitOfWork,
                "unitOfWork"
        );
        this.orderReader = Objects.requireNonNull(orderReader, "orderReader");

        OperationExecutionGuard prototypeAuthority =
                (merchantScope, principal, operation) -> {
                    if (PrototypeOrderingSubjectConfiguration.subjectsFor(
                                    merchantScope.merchantIdentifier()
                            ).isEmpty()
                            || !PrototypeMerchantRuntime.PROTOTYPE_PRINCIPAL.equals(
                                    principal.identifier()
                            )
                            || !"ordering.commit".equals(
                                    operation.operation().identifier()
                            )) {
                        throw new AuthorizationException(
                                "Prototype Ordering authority rejected execution"
                        );
                    }
                };

        OrderingApplicationService ordering = new OrderingApplicationService(
                orderingUnitOfWork,
                new InMemoryCustomerContextAuthority(),
                PrototypeJooqOrderingUseCase::resolvePrototypeCommitment,
                prototypeAuthority,
                Objects.requireNonNull(clock, "clock")
        );
        this.dispatcher = new ScopedOperationDispatcher<>(
                new ActiveOperationResolver(merchantRuntime.activation()),
                List.of(prototypeAuthority),
                ordering
        );
    }

    @Override
    public Order commit(
            String merchantIdentifier,
            String commandIdentifier,
            String orderIdentifier,
            List<RequestedOrderPortion> requestedPortions
    ) {
        MerchantScope merchantScope = trustedMerchantScope(merchantIdentifier);
        CommitOrderCommand command = new CommitOrderCommand(
                merchantScope,
                commandIdentifier,
                orderIdentifier,
                Optional.empty(),
                requestedPortions
        );
        dispatcher.dispatch(
                trustedContext(merchantScope),
                "ordering.commit",
                command
        );
        return orderReader.apply(merchantScope, orderIdentifier)
                .orElseThrow(() -> new IllegalStateException(
                        "Committed prototype Order is not readable"
                ));
    }

    @Override
    public Optional<Order> order(
            String merchantIdentifier,
            String orderIdentifier
    ) {
        MerchantScope merchantScope = trustedMerchantScope(merchantIdentifier);
        return orderReader.apply(merchantScope, orderIdentifier);
    }

    private MerchantScope trustedMerchantScope(String merchantIdentifier) {
        PrototypeMerchantView merchant = merchantRuntime.merchant(
                merchantIdentifier
        );
        if (PrototypeOrderingSubjectConfiguration.subjectsFor(
                merchant.merchantIdentifier()
        ).isEmpty()) {
            throw new IllegalArgumentException(
                    "Ordering is not exposed for this prototype merchant"
            );
        }
        return new MerchantScope(merchant.merchantIdentifier());
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

    private static ResolvedOrderCommitment resolvePrototypeCommitment(
            MerchantScope merchantScope,
            RequestedOrderPortion requested
    ) {
        PrototypeOrderingSubjectConfiguration.Subject subject =
                PrototypeOrderingSubjectConfiguration.byOrderableSubject(
                                merchantScope.merchantIdentifier(),
                                requested.subjectReference()
                        )
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Unknown prototype orderable subject"
                        ));
        long quantity = requested.quantity().magnitude().longValueExact();
        return new ResolvedOrderCommitment(
                new OrderCommitmentPortion(
                        requested.identifier(),
                        subject.orderableSubjectReference(),
                        requested.quantity(),
                        new MonetaryAmount(
                                new CurrencyIdentity("GBP"),
                                BigInteger.valueOf(subject.priceMinorUnits())
                        ),
                        subject.commitmentProvenance()
                ),
                List.of(new OrderInventoryClaimRequest(
                        "stock",
                        requested.identifier(),
                        new QuantityAllocationScope(
                                subject.stockSubjectReference(),
                                quantity
                        )
                ))
        );
    }
}
