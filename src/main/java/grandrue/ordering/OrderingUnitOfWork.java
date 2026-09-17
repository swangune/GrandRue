package grandrue.ordering;

import java.util.function.Function;

/** Transactional application boundary for one logical CommitOrder invocation. */
public interface OrderingUnitOfWork {
    OrderConfirmation execute(
            CommitOrderCommand command,
            Function<OrderingTransaction, OrderConfirmation> work
    );
}
