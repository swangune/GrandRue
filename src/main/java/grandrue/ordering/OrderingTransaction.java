package grandrue.ordering;

import grandrue.application.MerchantScope;
import grandrue.semantic.AllocationAuthority;
import grandrue.semantic.DomainEvent;

/** Narrow local consistency boundary for Order commitment. */
public interface OrderingTransaction extends AllocationAuthority {
    MerchantScope merchantScope();

    void recordOrder(Order order);

    void appendPendingEvent(DomainEvent event);
}
