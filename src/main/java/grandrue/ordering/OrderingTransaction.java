package grandrue.ordering;

import grandrue.application.MerchantScope;
import mainstreet.semantic.AllocationAuthority;
import mainstreet.semantic.DomainEvent;

/** Narrow local consistency boundary for Order commitment. */
public interface OrderingTransaction extends AllocationAuthority {
    MerchantScope merchantScope();

    void recordOrder(Order order);

    void appendPendingEvent(DomainEvent event);
}
