package grandrue.ordering;

import mainstreet.semantic.AllocationClaim;
import mainstreet.semantic.DomainEvent;

import java.util.List;
import java.util.Objects;

/** Exact committed result for one logical CommitOrder invocation. */
public record OrderConfirmation(
        Order order,
        List<AllocationClaim> inventoryClaims,
        DomainEvent pendingEvent
) {
    public OrderConfirmation {
        Objects.requireNonNull(order);
        inventoryClaims = List.copyOf(
                Objects.requireNonNull(inventoryClaims)
        );
        Objects.requireNonNull(pendingEvent);
    }
}
