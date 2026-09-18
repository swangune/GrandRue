package grandrue.ordering;

import grandrue.customer.CustomerContextAuthority;
import mainstreet.runtime.AuthorizationException;
import grandrue.runtime.CapabilityOperationHandler;
import mainstreet.runtime.OperationExecutionContext;
import mainstreet.runtime.OperationExecutionGuard;
import grandrue.runtime.OperationFulfilment;
import mainstreet.semantic.AllocationClaim;
import mainstreet.semantic.DomainEvent;
import mainstreet.semantic.executable.ExecutableAllocationClaimEffect;
import mainstreet.semantic.executable.ExecutableObjectCreationEffect;
import mainstreet.semantic.executable.ExecutableOperationalObjectTypeIdentity;
import mainstreet.semantic.executable.ExecutableOperationEffect;
import mainstreet.semantic.executable.ExecutableRelationshipEstablishmentEffect;

import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Capability-owned application coordinator for MS-PROT-077 CommitOrder.
 * Current terms are revalidated through an explicit port; Inventory claims are
 * performed through the local OrderingTransaction without transferring stock
 * ownership to Ordering.
 */
public final class OrderingApplicationService
        implements CapabilityOperationHandler<CommitOrderCommand> {

    private static final String OPERATION_IDENTIFIER = "ordering.commit";
    private static final ExecutableOperationalObjectTypeIdentity ORDER_OBJECT =
            new ExecutableOperationalObjectTypeIdentity("ordering", "order");
    private static final Set<String> COMMITTED_EVENT_IDENTIFIERS =
            Set.of("order.committed");

    private final OrderingUnitOfWork unitOfWork;
    private final CustomerContextAuthority customerContexts;
    private final OrderCommitmentRevalidator commitmentRevalidator;
    private final OperationExecutionGuard finalAuthorityGuard;
    private final Clock platformClock;

    /** Runtime composition must provide explicit authority before fulfilment. */
    public OrderingApplicationService(
            OrderingUnitOfWork unitOfWork,
            CustomerContextAuthority customerContexts,
            OrderCommitmentRevalidator commitmentRevalidator
    ) {
        this(
                unitOfWork,
                customerContexts,
                commitmentRevalidator,
                (merchantScope, principal, operation) -> {
                    throw new AuthorizationException(
                            "Runtime actor authorisation authority is not configured"
                    );
                },
                Clock.systemUTC()
        );
    }

    public OrderingApplicationService(
            OrderingUnitOfWork unitOfWork,
            CustomerContextAuthority customerContexts,
            OrderCommitmentRevalidator commitmentRevalidator,
            OperationExecutionGuard finalAuthorityGuard,
            Clock platformClock
    ) {
        this.unitOfWork = Objects.requireNonNull(unitOfWork);
        this.customerContexts = Objects.requireNonNull(customerContexts);
        this.commitmentRevalidator = Objects.requireNonNull(
                commitmentRevalidator
        );
        this.finalAuthorityGuard = Objects.requireNonNull(finalAuthorityGuard);
        this.platformClock = Objects.requireNonNull(platformClock);
    }

    @Override
    public OperationFulfilment fulfill(
            CommitOrderCommand command,
            OperationExecutionContext context
    ) {
        Objects.requireNonNull(command);
        Objects.requireNonNull(context);
        if (!command.merchantScope().equals(context.merchantScope())) {
            throw new IllegalArgumentException(
                    "Order command belongs to another merchant scope"
            );
        }
        if (!OPERATION_IDENTIFIER.equals(
                context.applicableOperation().operation().identifier()
        )) {
            throw new IllegalArgumentException(
                    "Ordering handler does not own operation: "
                            + context.applicableOperation()
                            .operation().identifier()
            );
        }

        OperationFulfilment[] preCommitFulfilment = new OperationFulfilment[1];
        OrderConfirmation confirmation = commit(
                command,
                context.applicableOperation().releaseIdentifier(),
                () -> finalAuthorityGuard.validate(
                        context.merchantScope(),
                        context.principal(),
                        context.applicableOperation()
                ),
                committed -> preCommitFulfilment[0] =
                        OperationFulfilment.conformingTo(
                                context.applicableOperation(),
                                fulfilledEffects(command, committed),
                                COMMITTED_EVENT_IDENTIFIERS
                        )
        );

        // An idempotent replay returns the already committed confirmation
        // before invoking the work callback; conformance can be checked here
        // because no new mutation occurred on that replay.
        if (preCommitFulfilment[0] == null) {
            preCommitFulfilment[0] = OperationFulfilment.conformingTo(
                    context.applicableOperation(),
                    fulfilledEffects(command, confirmation),
                    COMMITTED_EVENT_IDENTIFIERS
            );
        }
        return preCommitFulfilment[0];
    }

    OrderConfirmation commit(CommitOrderCommand command) {
        return commit(command, "test-only:unpublished-release");
    }

    OrderConfirmation commit(
            CommitOrderCommand command,
            String governingReleaseIdentifier
    ) {
        return commit(
                command,
                governingReleaseIdentifier,
                () -> { },
                ignored -> { }
        );
    }

    private OrderConfirmation commit(
            CommitOrderCommand command,
            String governingReleaseIdentifier,
            Runnable finalAuthorityValidation,
            Consumer<OrderConfirmation> beforeCommitValidation
    ) {
        Objects.requireNonNull(command);
        requireIdentifier(
                governingReleaseIdentifier,
                "Governing release identifier"
        );
        Objects.requireNonNull(finalAuthorityValidation);
        Objects.requireNonNull(beforeCommitValidation);

        return unitOfWork.execute(command, transaction -> {
            finalAuthorityValidation.run();
            command.customerContextIdentifier().ifPresent(identifier ->
                    customerContexts.require(
                            command.merchantScope(),
                            identifier
                    )
            );

            List<ResolvedOrderCommitment> resolved = new ArrayList<>();
            for (RequestedOrderPortion requested
                    : command.requestedPortions()) {
                ResolvedOrderCommitment result = Objects.requireNonNull(
                        commitmentRevalidator.revalidate(
                                command.merchantScope(),
                                requested
                        )
                );
                validateResolvedPortion(requested, result.commitmentPortion());
                resolved.add(result);
            }

            Instant recordedAt = platformClock.instant();
            List<AllocationClaim> claims = new ArrayList<>();
            Set<String> claimIdentifiers = new HashSet<>();
            for (ResolvedOrderCommitment result : resolved) {
                for (OrderInventoryClaimRequest requirement
                        : result.requiredInventoryClaims()) {
                    String claimIdentifier = command.identifier()
                            + ":inventory:"
                            + requirement.commitmentPortionIdentifier()
                            + ":"
                            + requirement.identifier();
                    if (!claimIdentifiers.add(claimIdentifier)) {
                        throw new IllegalArgumentException(
                                "Resolved Order contains duplicate Inventory claim requirement"
                        );
                    }
                    claims.add(transaction.claim(
                            claimIdentifier,
                            requirement.scope(),
                            requirement.commitmentPortionIdentifier(),
                            recordedAt
                    ));
                }
            }

            Order order = new Order(
                    command.merchantScope(),
                    command.orderIdentifier(),
                    command.customerContextIdentifier(),
                    resolved.stream()
                            .map(ResolvedOrderCommitment::commitmentPortion)
                            .toList(),
                    governingReleaseIdentifier,
                    recordedAt
            );
            transaction.recordOrder(order);

            DomainEvent event = new DomainEvent(
                    command.identifier() + ":order-committed",
                    "order.committed",
                    command.orderIdentifier(),
                    command.identifier(),
                    recordedAt
            );
            transaction.appendPendingEvent(event);

            OrderConfirmation confirmation = new OrderConfirmation(
                    order,
                    claims,
                    event
            );
            beforeCommitValidation.accept(confirmation);
            return confirmation;
        });
    }

    private static List<ExecutableOperationEffect> fulfilledEffects(
            CommitOrderCommand command,
            OrderConfirmation confirmation
    ) {
        List<ExecutableOperationEffect> effects = new ArrayList<>();
        effects.add(new ExecutableObjectCreationEffect(
                ORDER_OBJECT,
                Optional.empty()
        ));
        if (command.customerContextIdentifier().isPresent()) {
            effects.add(new ExecutableRelationshipEstablishmentEffect(
                    "ordering.customer-context"
            ));
        }
        if (!confirmation.inventoryClaims().isEmpty()) {
            effects.add(new ExecutableAllocationClaimEffect(
                    "inventory.stock"
            ));
        }
        return List.copyOf(effects);
    }

    private static void validateResolvedPortion(
            RequestedOrderPortion requested,
            OrderCommitmentPortion committed
    ) {
        if (!requested.identifier().equals(committed.identifier())
                || !requested.subjectReference().equals(
                        committed.committedSubjectReference()
                )
                || !requested.quantity().equals(committed.quantity())) {
            throw new IllegalArgumentException(
                    "Authoritative Order commitment resolution changed requested identity, subject or quantity"
            );
        }
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
