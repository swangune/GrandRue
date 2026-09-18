package grandrue.ordering;

import grandrue.application.MerchantScope;
import grandrue.inventory.InMemoryQuantityAllocationAuthority;
import grandrue.semantic.AllocationClaim;
import grandrue.semantic.AllocationScope;
import grandrue.semantic.DomainEvent;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * In-memory atomicity adapter for Ordering tests/local execution.
 *
 * <p>Inventory arithmetic remains owned by InMemoryQuantityAllocationAuthority.
 * This unit of work reconstructs a staged Inventory-owned authority from
 * committed claims and publishes the staged claim set only when Order/event
 * work succeeds, modelling the accepted local cross-capability transaction.</p>
 */
public final class InMemoryOrderingUnitOfWork
        implements OrderingUnitOfWork {

    private final Map<String, Long> initialStockOnHand;
    private final Map<MerchantScope, InMemoryQuantityAllocationAuthority>
            committedInventories = new LinkedHashMap<>();
    private State state = State.empty();

    public InMemoryOrderingUnitOfWork(
            Map<String, Long> initialStockOnHand
    ) {
        this.initialStockOnHand = Map.copyOf(
                Objects.requireNonNull(initialStockOnHand)
        );
        // Validate stock semantics through the Inventory-owned implementation.
        new InMemoryQuantityAllocationAuthority(this.initialStockOnHand);
    }

    @Override
    public synchronized OrderConfirmation execute(
            CommitOrderCommand command,
            Function<OrderingTransaction, OrderConfirmation> work
    ) {
        Objects.requireNonNull(command);
        Objects.requireNonNull(work);

        HandledCommandKey handledKey = new HandledCommandKey(
                command.merchantScope(),
                command.identifier()
        );
        HandledCommand handled = state.handledCommands().get(handledKey);
        if (handled != null) {
            if (!handled.command().equals(command)) {
                throw new OrderCommandIdentityConflictException(
                        command.identifier()
                );
            }
            return handled.confirmation();
        }

        State[] stagedState = {state};
        InMemoryQuantityAllocationAuthority stagedInventory =
                rebuildInventory(command.merchantScope());

        OrderingTransaction transaction = new OrderingTransaction() {
            @Override
            public MerchantScope merchantScope() {
                return command.merchantScope();
            }

            @Override
            public AllocationClaim claim(
                    String identifier,
                    AllocationScope scope,
                    String useIdentifier,
                    Instant claimedAt
            ) {
                AllocationClaim claim = stagedInventory.claim(
                        identifier,
                        scope,
                        useIdentifier,
                        claimedAt
                );
                stagedState[0] = stagedState[0].withInventoryClaim(
                        command.merchantScope(),
                        claim
                );
                return claim;
            }

            @Override
            public void recordOrder(Order order) {
                Objects.requireNonNull(order);
                if (!command.merchantScope().equals(order.merchantScope())) {
                    throw new IllegalArgumentException(
                            "Order belongs to another merchant scope"
                    );
                }
                if (!command.orderIdentifier().equals(order.identifier())) {
                    throw new IllegalArgumentException(
                            "Order identity differs from CommitOrder intent"
                    );
                }
                stagedState[0] = stagedState[0].withOrder(order);
            }

            @Override
            public void appendPendingEvent(DomainEvent event) {
                Objects.requireNonNull(event);
                stagedState[0] = stagedState[0].withPendingEvent(
                        command.merchantScope(),
                        event
                );
            }
        };

        OrderConfirmation confirmation = work.apply(transaction);
        Objects.requireNonNull(confirmation);
        if (!command.merchantScope().equals(
                confirmation.order().merchantScope()
        ) || !command.orderIdentifier().equals(
                confirmation.order().identifier()
        )) {
            throw new IllegalStateException(
                    "Ordering transaction returned a different Order identity"
            );
        }

        stagedState[0] = stagedState[0].withHandledCommand(
                handledKey,
                new HandledCommand(command, confirmation)
        );
        state = stagedState[0];
        committedInventories.put(
                command.merchantScope(),
                stagedInventory
        );
        return confirmation;
    }

    public synchronized Optional<Order> order(
            MerchantScope merchantScope,
            String identifier
    ) {
        return Optional.ofNullable(
                state.orders().get(new OrderKey(merchantScope, identifier))
        );
    }

    public synchronized Optional<DomainEvent> pendingEvent(
            MerchantScope merchantScope,
            String identifier
    ) {
        return Optional.ofNullable(
                state.pendingEvents().get(
                        new EventKey(merchantScope, identifier)
                )
        );
    }

    public synchronized Optional<AllocationClaim> inventoryClaim(
            MerchantScope merchantScope,
            String identifier
    ) {
        return Optional.ofNullable(
                state.inventoryClaimsFor(merchantScope).get(identifier)
        );
    }

    public synchronized Optional<AllocationClaim> inventoryClaim(
            String identifier
    ) {
        AllocationClaim result = null;
        for (Map<String, AllocationClaim> claims
                : state.inventoryClaimsByMerchant().values()) {
            AllocationClaim candidate = claims.get(identifier);
            if (candidate != null) {
                if (result != null) {
                    throw new IllegalStateException(
                            "Inventory claim identifier is ambiguous across merchants"
                    );
                }
                result = candidate;
            }
        }
        return Optional.ofNullable(result);
    }

    public synchronized long availableToPromise(
            MerchantScope merchantScope,
            String subjectIdentifier
    ) {
        InMemoryQuantityAllocationAuthority inventory =
                committedInventories.get(merchantScope);
        if (inventory == null) {
            inventory = rebuildInventory(merchantScope);
        }
        return inventory.availableToPromise(subjectIdentifier);
    }

    /** Convenience for tests using exactly one merchant inventory context. */
    public synchronized long availableToPromise(String subjectIdentifier) {
        if (committedInventories.size() > 1) {
            throw new IllegalStateException(
                    "Merchant scope is required when multiple inventories exist"
            );
        }
        if (committedInventories.size() == 1) {
            return committedInventories.values().iterator().next()
                    .availableToPromise(subjectIdentifier);
        }
        return new InMemoryQuantityAllocationAuthority(initialStockOnHand)
                .availableToPromise(subjectIdentifier);
    }

    private InMemoryQuantityAllocationAuthority rebuildInventory(
            MerchantScope merchantScope
    ) {
        InMemoryQuantityAllocationAuthority inventory =
                new InMemoryQuantityAllocationAuthority(initialStockOnHand);
        for (AllocationClaim claim
                : state.inventoryClaimsFor(merchantScope).values()) {
            inventory.claim(
                    claim.identifier(),
                    claim.scope(),
                    claim.useIdentifier(),
                    claim.claimedAt()
            );
        }
        return inventory;
    }

    private record OrderKey(
            MerchantScope merchantScope,
            String identifier
    ) { }

    private record EventKey(
            MerchantScope merchantScope,
            String identifier
    ) { }

    private record HandledCommandKey(
            MerchantScope merchantScope,
            String identifier
    ) { }

    private record HandledCommand(
            CommitOrderCommand command,
            OrderConfirmation confirmation
    ) { }

    private record State(
            Map<OrderKey, Order> orders,
            Map<EventKey, DomainEvent> pendingEvents,
            Map<MerchantScope, Map<String, AllocationClaim>>
                    inventoryClaimsByMerchant,
            Map<HandledCommandKey, HandledCommand> handledCommands
    ) {
        private static State empty() {
            return new State(Map.of(), Map.of(), Map.of(), Map.of());
        }

        private Map<String, AllocationClaim> inventoryClaimsFor(
                MerchantScope merchantScope
        ) {
            return inventoryClaimsByMerchant.getOrDefault(
                    merchantScope,
                    Map.of()
            );
        }

        private State withOrder(Order order) {
            OrderKey key = new OrderKey(
                    order.merchantScope(),
                    order.identifier()
            );
            if (orders.containsKey(key)) {
                throw new IllegalArgumentException(
                        "Order identifier already used for merchant: "
                                + order.identifier()
                );
            }
            Map<OrderKey, Order> updated = new LinkedHashMap<>(orders);
            updated.put(key, order);
            return new State(
                    Map.copyOf(updated),
                    pendingEvents,
                    inventoryClaimsByMerchant,
                    handledCommands
            );
        }

        private State withPendingEvent(
                MerchantScope merchantScope,
                DomainEvent event
        ) {
            EventKey key = new EventKey(merchantScope, event.identifier());
            if (pendingEvents.containsKey(key)) {
                throw new IllegalArgumentException(
                        "Pending event identifier already used for merchant: "
                                + event.identifier()
                );
            }
            Map<EventKey, DomainEvent> updated =
                    new LinkedHashMap<>(pendingEvents);
            updated.put(key, event);
            return new State(
                    orders,
                    Map.copyOf(updated),
                    inventoryClaimsByMerchant,
                    handledCommands
            );
        }

        private State withInventoryClaim(
                MerchantScope merchantScope,
                AllocationClaim claim
        ) {
            Map<String, AllocationClaim> merchantClaims =
                    new LinkedHashMap<>(inventoryClaimsFor(merchantScope));
            if (merchantClaims.putIfAbsent(claim.identifier(), claim) != null) {
                throw new IllegalArgumentException(
                        "Inventory claim identifier already used for merchant: "
                                + claim.identifier()
                );
            }
            Map<MerchantScope, Map<String, AllocationClaim>> updated =
                    new LinkedHashMap<>(inventoryClaimsByMerchant);
            updated.put(merchantScope, Map.copyOf(merchantClaims));
            return new State(
                    orders,
                    pendingEvents,
                    Map.copyOf(updated),
                    handledCommands
            );
        }

        private State withHandledCommand(
                HandledCommandKey key,
                HandledCommand handledCommand
        ) {
            Map<HandledCommandKey, HandledCommand> updated =
                    new LinkedHashMap<>(handledCommands);
            updated.put(key, handledCommand);
            return new State(
                    orders,
                    pendingEvents,
                    inventoryClaimsByMerchant,
                    Map.copyOf(updated)
            );
        }
    }
}
